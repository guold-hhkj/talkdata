package com.hhkj.talkdata.network.netlayer.base.socket;



import android.util.Log;

import com.hhkj.talkdata.network.netlayer.base.common.ObjectID;
import com.hhkj.talkdata.network.netlayer.base.common.OidCounter;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by guold on 2016/7/4.
 */
public class ConnImpl {
    /**
     * 主机名、端口号
     */
    private String hostName;
    private int port;
    /**
     * 任务处理线程和其运行标志
     */
    private Thread msgHandleThread = null;
    private boolean msgHandleThread_OverFlag = false;
    private Runnable msgHandleRunnable;
    /**
     * 消息接收线程和其运行标志
     */
    private Thread msgRWThread = null;
    private boolean msgRWThread_OverFlag = false;
    private Runnable msgRWRunnable;
    /**
     * 内部装载所有接收到的消息
     */
    private ArrayBlockingQueue<Msg> readMsgs = null;
    /**
     * 内部装载所有需要发送的消息
     */
    private ArrayBlockingQueue<Msg> writeMsgs = null;
    /**
     * 所有长链接收发合一消息任务
     */
    private ArrayBlockingQueue<MsgTask> allTasks = null;
    /**
     * 默认的，消息处理方法
     */
    private MsgTask.StatusListener defaultStatusListener;
    /**
     * 代表这个链接的sessionkey
     */
    private ObjectID sessionKey;
    /**
     * 生成唯一的sessionkey
     */
    private static OidCounter oidCounter = new OidCounter();

    private ConnImpl() {
        sessionKey = new ObjectID(new Date(), oidCounter.getNum());
    }

    private ConnImpl init(final String url, final MsgTask.StatusListener defaultStatusListener, final OnConnectionListener connectionListener) {
        this.defaultStatusListener = defaultStatusListener;
        readMsgs = new ArrayBlockingQueue<>(1000);
        writeMsgs = new ArrayBlockingQueue<>(1000);
        allTasks = new ArrayBlockingQueue<>(1000);
        try {
            hostName = url.split(":")[0];
            port = Integer.parseInt(url.split(":")[1]);
        } catch (Exception e) {
            return null;
        }
        if (hostName == null || hostName.length() < 3) {
            return null;
        }

        msgRWRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //打开Socket通道
                    SocketChannel client = SocketChannel.open();
                    //设置为非阻塞模式
                    client.configureBlocking(false);
                    //打开选择器
                    Selector selector = Selector.open();
                    //注册连接服务端socket动作
                    SelectionKey cKey = client.register(selector, SelectionKey.OP_CONNECT);
                    //连接
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(hostName, port);
                    boolean connRlt = client.connect(inetSocketAddress);
                    while (true){
                        selector.select();
                        if(cKey.isConnectable()){
                            break;
                        }
                    }
                    if (client.isConnectionPending())
                        client.finishConnect();
                    boolean isConnected = client.isConnected();
                    if(isConnected){
                        // 链接成功，给通道添加读写权限
                        client.register(selector, SelectionKey.OP_WRITE| SelectionKey.OP_READ);
                        if(connectionListener!=null){
                            connectionListener.onSuccess();
                        }
                    }
                    //分配内存，用来缓存服务端发送过来的消息
                    ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
                    //分配内存，用来处理消息协议头
                    byte[] msgHead = new byte[20];
                    int total = 0;
                    while (true) {
                        if (msgRWThread_OverFlag) {
                            break;
                        }
                        selector.select();
                        boolean noMsgToSend = false;
                        if (cKey.isWritable()) {
                            // 有消息要发送给服务端，发送
                            SocketChannel channel = (SocketChannel) cKey.channel();
                            Msg msg = null;
                            if (writeMsgs != null && writeMsgs.size() > 0) {
                                msg = writeMsgs.poll();
                                channel.write(ByteBuffer.wrap(msg.toBytes()));
                                Log.d("socket", new String(msg.toBytes()));
                            } else {
                                noMsgToSend = true;
                            }
                        } else {
                            noMsgToSend = true;
                        }

                        boolean noMsgToRecv = false;
                        if (cKey.isReadable()) {
                            // 有服务端发送过来的消息可读，读
                            SocketChannel channel = (SocketChannel) cKey.channel();
                            int count = channel.read(buffer);
                            if (count > 0) {
                                total += count;
                                buffer.flip();
                                while (buffer.remaining() > 0) {
                                    try {
                                        Msg msg = new Msg();
                                        byte[] msgContent = new byte[buffer.remaining()];
                                        buffer.get(msgContent);
                                        msg.setMsgBody(new MsgBody(msgContent));
                                        readMsgs.put(msg);
                                    } catch (Exception e) {
                                        //解析协议消息出错，直接退出本次解析
                                        Log.d(this.getClass().getName(),e.getMessage());
                                        break;
                                    }
                                }
                                buffer.clear();
                            } else{
                                noMsgToRecv = true;
                            }
                        } else {
                            noMsgToRecv = true;
                        }
                        if(noMsgToRecv && noMsgToSend){
                            // 如果没有消息要读和写，休息200ms
                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException e) {
                                Log.d(this.getClass().getName(),e.getMessage());
                            }
                        }
                    }
                    cKey.cancel();
                    client.close();
                } catch (Exception e) {
                    Log.d(this.getClass().getName(),e.getMessage());
                }
                msgRWThread_OverFlag = false;
            }
        };

        msgHandleRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (msgHandleThread_OverFlag) {
                            break;
                        }
                        if (readMsgs != null && readMsgs.size() > 0) {
                            Msg msg = readMsgs.poll();
                            Log.d("socket", msg.getMsgBody().toString());
                            int msgId = msg.getMsgId();
                            boolean findListener = false;
                            for (MsgTask x : allTasks) {
                                if (x.getReq().getMsgId() == msgId) {
                                    // 删除这个任务
                                    allTasks.remove(x);
                                    x.setRsp(msg);
                                    if (x.getStatusListener() != null) {
                                        try {
                                            x.getStatusListener().onSuccess(x.getRsp(), x.getReq());
                                        } catch (Exception e) {
                                            try {
                                                x.getStatusListener().onError(ConnConfig.CONN_ONSUCCESS_ERROR, x.getReq());
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    }
                                    findListener = true;
                                }
                                if (System.currentTimeMillis() - x.getTaskSubmitTime() > 1000 * 20) {
                                    // 已经过了任务最多等待时间了，删除这个任务
                                    allTasks.remove(x);
                                }
                            }
                            if (findListener) {
                                // 已经删除过了
                            } else {
                                // 没有找到，建立临时的交给默认处理器处理
                                MsgTask tmp = new MsgTask(null, msg, defaultStatusListener);
                                if (tmp.getStatusListener() != null) {
                                    try {
                                        tmp.getStatusListener().onSuccess(tmp.getRsp(), tmp.getReq());
                                    } catch (Exception e) {
                                        try {
                                            tmp.getStatusListener().onError(ConnConfig.CONN_ONSUCCESS_ERROR, tmp.getReq());
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d(this.getClass().getName(),e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
                msgHandleThread_OverFlag = false;
            }
        };

        msgHandleThread = new Thread(msgHandleRunnable);
        msgHandleThread.start();
        msgRWThread = new Thread(msgRWRunnable);
        msgRWThread.start();
        return this;
    }

    public synchronized String getSessionKey() {
        return sessionKey.toHexString();
    }

    public synchronized static ConnImpl newInstance(final String url, final MsgTask.StatusListener defaultStatusListener, OnConnectionListener connectionListener) {
        return new ConnImpl().init(url, defaultStatusListener, connectionListener);
    }

    public synchronized void postMsg(MsgTask msgTask) {
        try {
            allTasks.put(msgTask);
            writeMsgs.put(msgTask.getReq());
        } catch (InterruptedException e) {
            Log.d(this.getClass().getName(),e.getMessage());
        }
    }

    public synchronized void destory() {
        msgHandleThread_OverFlag = true;
        msgRWThread_OverFlag = true;

        long currTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - currTime > 2000) {
                break;
            }
            if(!msgHandleThread_OverFlag && !msgRWThread_OverFlag){
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.d(this.getClass().getName(),e.getMessage());
            }
        }
        // 清空消息队列
        readMsgs.clear();
        writeMsgs.clear();
        allTasks.clear();
    }

    // 重连，用在网络情况发生变化的时候
    public synchronized void relink(){
        // 先让工作线程正常结束，正常结束后自动恢复这两个变量的状态
        if(!msgHandleThread_OverFlag){
            msgHandleThread_OverFlag = true;
        }
        if(!msgRWThread_OverFlag){
            msgRWThread_OverFlag = true;
        }

        // 等待结束
        long currTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - currTime > 2000) {
                break;
            }
            if(!msgHandleThread_OverFlag && !msgRWThread_OverFlag){
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.d(this.getClass().getName(),e.getMessage());
            }
        }
        rework();
    }

    public synchronized void rework(){
        // 重启工作线程
        if(!msgHandleThread.isAlive()){
            msgHandleThread = new Thread(msgHandleRunnable);
            msgHandleThread.start();
        }
        if(!msgRWThread.isAlive()){
            msgRWThread = new Thread(msgRWRunnable);
            msgRWThread.start();
        }
    }

    public synchronized boolean isWorkOK(){
        return msgRWThread.isAlive() && msgHandleThread.isAlive();
    }
}
