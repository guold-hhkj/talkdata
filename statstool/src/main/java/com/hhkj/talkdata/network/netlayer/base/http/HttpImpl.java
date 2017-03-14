package com.hhkj.talkdata.network.netlayer.base.http;



import android.util.Log;

import com.hhkj.talkdata.network.netlayer.base.common.MultiMemberGZIPInputStream;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by guold .
 * Date: 2015/11/24
 * Desc: fill it
 */
public class HttpImpl {
    // 唯一对象
    private static HttpImpl theHttpImpl = null;

    private ThreadPoolExecutor theWorkers;

    private HttpImpl() {
        theWorkers = new ThreadPoolExecutor(HttpConfig.COUNT_HTTP_THREAD, HttpConfig.MAX_COUNT_HTTP_THREAD, HttpConfig.KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(HttpConfig.WORK_QUEUE_CONUT),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    // 线程安全
    synchronized public static HttpImpl getInstance() {
        if (theHttpImpl == null) {
            theHttpImpl = new HttpImpl();
        }
        return theHttpImpl;
    }

    public void req(final HttpTask httpTask, final boolean isGizp) {
        theWorkers.execute(new Runnable() {
            @Override
            public void run() {
                String reqUrl = httpTask.getReqUrl();
                if (reqUrl.indexOf("https") != -1) {
                    doHttpsReq(httpTask, isGizp);
                } else {
                    doHttpReq(httpTask, isGizp);
                }
            }
        });
    }

    /**
     * 发https请求
     *
     * @param httpTask
     * @param isGizp
     */
    private void doHttpsReq(HttpTask httpTask, boolean isGizp) {
        if (httpTask == null) {
            return;
        }
        PrintWriter out = null;
        HttpURLConnection urlConnection = HttpsBuilder.getHttpsURLConnection(httpTask.getReqUrl());
        if (urlConnection == null) {
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_INIT_NET_ERROR);
            }
            return;
        }
        dealWithConnection(httpTask, isGizp, out, urlConnection);
    }

    /**
     * 发http请求
     *
     * @param httpTask
     * @param isGizp
     */
    private void doHttpReq(HttpTask httpTask, boolean isGizp) {
        if (httpTask == null) {
            return;
        }
        PrintWriter out = null;
        HttpURLConnection urlConnection = null;
        // 1.URL解码
        URL url;
        try {
            url = new URL(httpTask.getReqUrl());
        } catch (MalformedURLException e) {
            Log.e(this.toString(),e.toString());
            url = null;
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_URL_NET_ERROR);
            }
            return;
        }
        // 2.初始化HTTP链接
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.e(this.toString(),e.toString());
            urlConnection = null;
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_INIT_NET_ERROR);
            }
            return;
        }
        dealWithConnection(httpTask, isGizp, out, urlConnection);
    }

    /**
     * 处理连接
     *
     * @param httpTask
     * @param isGizp
     * @param out
     * @param urlConnection
     */
    private void dealWithConnection(HttpTask httpTask, boolean isGizp, PrintWriter out, HttpURLConnection urlConnection) {

        // 3.配置HTTP链接参数
        try {
            // setConnectTimeout指与服务器建立连接的超时时间
            urlConnection.setConnectTimeout(HttpConfig.CONNECT_TIMEOUT);
            // setReadTimeout建立连接后如果指定时间内服务器没有返回数据，当做超时处理
            urlConnection.setReadTimeout(HttpConfig.CONNECT_TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.addRequestProperty("Accept-Encoding", "max-age=0");
            // 是否需要gzip压缩
            urlConnection.setRequestProperty("Accept-Encoding", isGizp ? "gzip" : "");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            // 与后台全部用POST请求
            urlConnection.setRequestMethod("POST");
        } catch (Exception e) {
            Log.e(this.toString(),e.toString());
            urlConnection = null;
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_CONN_CONFIG_NET_ERROR);
            }
            return;
        }
        // 4.写出接口参数
        try {
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(urlConnection.getOutputStream());
            httpTask.setReqTime(System.currentTimeMillis());
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onSendProgressChange(0);
            }
            // 发送请求参数
            out.print(httpTask.getReqParam());
            // flush输出流的缓冲
            out.flush();
            out.close();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onSendProgressChange(100);
            }
            httpTask.setReqSOTime(System.currentTimeMillis());
            // 打印请求参数
            Log.i("request_url", httpTask.getReqUrl());
            Log.i("request_param", httpTask.getReqParam());
        } catch (Exception e) {
            Log.e(this.toString(),e.toString());
            if (out != null) out.close();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_SEND_PARAM_NET_ERROR);
            }
            return;
        }
        // 5.链接到服务器
        try {
            urlConnection.connect();
        } catch (UnknownHostException e) {
            Log.e(this.toString(),e.toString());
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_CANT_FIND_SVR_NET_ERROR);
            }
            return;
        } catch (SocketTimeoutException e) {
            Log.e(this.toString(),e.toString());
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_TIME_OUT_NET_ERROR);
            }
            return;
        } catch (IOException e) {
            Log.e(this.toString(),e.toString());
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_SOCKET_IO_NET_ERROR);
            }
            return;
        } catch (Exception e) {
            Log.e(this.toString(),e.toString());
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_EXCEPTION_NET_ERROR);
            }
            return;
        }
        // 6.得到服务器状态返回码
        int rspCode;
        try {
            rspCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            Log.e(this.toString(),e.toString());
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_SVR_RSP_HEAD_NET_ERROR);
            }
            return;
        }
        // 7.判断返回码是否正确
        if (rspCode != HttpURLConnection.HTTP_OK) {
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_SVR_RTN_CODE_NET_ERROR);
            }
            return;
        }
        // 8.得到输入流,并且得到返回HTTP报文头参数
        InputStream responseIn = null;
        int totalLength = 0;
        try {
            responseIn = urlConnection.getInputStream();
            Map<String, List<String>> responseParam = urlConnection.getHeaderFields();
            // 得到长度;
            if (responseParam.get("Content-Length") != null
                    && responseParam.get("Content-Length") instanceof List
                    && responseParam.get("Content-Length").size() > 0) {
                String length = responseParam.get("Content-Length").get(0);
                try {
                    totalLength = Integer.parseInt(length);
                } catch (Exception e) {
                    totalLength = 0;
                }
            }
        } catch (IOException e) {
            Log.e(this.toString(),e.toString());
            if (responseIn != null) try {
                responseIn.close();
            } catch (IOException e1) {
                Log.e(this.toString(), e1.toString());
            }
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_GET_INPUT_STREAM_NET_ERROR);
            }
            return;
        }
        // 9.数据正常请求回来时才进行解析
        String key = urlConnection.getHeaderField("Content-Encoding");
        if (responseIn != null && key != null && "gzip".equals(key)) {
            try {
                responseIn = new MultiMemberGZIPInputStream(responseIn);
            } catch (IOException e) {
                Log.e(this.toString(),e.toString());
                if (responseIn != null) try {
                    responseIn.close();
                } catch (IOException e1) {
                    Log.e(this.toString(), e1.toString());
                }
                if (urlConnection != null) urlConnection.disconnect();
                if (httpTask.getStatusListener() != null) {
                    httpTask.getStatusListener().onError(HttpConfig.HTTP_CONVERT_ZIP_NET_ERROR);
                }
                return;
            }
        }
        // 10.正式接收数据
        byte[] buffer = null;
        ByteArrayOutputStream baos = null;
        try {
            final int BUF_SIZE = 1024;
            byte[] data = new byte[BUF_SIZE];
            int len = 0;
            int nowRcv = 0;
            long lastCallTime = System.currentTimeMillis();
            long nowCallTime = 0;
            baos = new ByteArrayOutputStream();
            try {
                httpTask.setRspTime(System.currentTimeMillis());
                if (httpTask.getStatusListener() != null) {
                    httpTask.getStatusListener().onReceiveProgressChange(0);
                }
                while ((len = responseIn.read(data)) != -1) {
                    baos.write(data, 0, len);
                    if (totalLength > 0) {
                        nowRcv += len;
                        nowCallTime = System.currentTimeMillis();
                        if ((nowCallTime - lastCallTime) > 300 && httpTask.getStatusListener() != null) {
                            lastCallTime = nowCallTime;
                            int per = (int) (100.0 * nowRcv / totalLength);
                            httpTask.getStatusListener().onReceiveProgressChange(per);
                        }
                    }
                }
            } catch (EOFException e) {
                // 经测试遇到EOFException时，数据已读取完毕。
                Log.e(this.toString(),e.toString());
            }
            baos.flush();
            httpTask.setRspROTime(System.currentTimeMillis());
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onReceiveProgressChange(100);
            }
            buffer = baos.toByteArray();
            baos.close();
        } catch (Exception e) {
            Log.e(this.toString(),e.toString());
            if (baos != null) try {
                baos.close();
            } catch (IOException e1) {
                Log.e(this.toString(), e1.toString());
            }
            if (responseIn != null) try {
                responseIn.close();
            } catch (IOException e1) {
                Log.e(this.toString(), e1.toString());
            }
            if (urlConnection != null) urlConnection.disconnect();
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_READ_INPUT_NET_ERROR);
            }
            return;
        } finally {
            if (baos != null) try {
                baos.close();
            } catch (IOException e) {
                Log.e(this.toString(), e.toString());
            }
            if (responseIn != null) try {
                responseIn.close();
            } catch (IOException e) {
                Log.e(this.toString(), e.toString());
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
        // 11.转换字符串！
        String response = null;
        try {
            response = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.toString(),e.toString());
            if (httpTask.getStatusListener() != null) {
                httpTask.getStatusListener().onError(HttpConfig.HTTP_TO_STRING_NET_ERROR);
            }
        }
        // 打印返回参数
        Log.i("request_back", response);
        // 12.填充httpTask内容
        httpTask.setRspParam(response);
        if (httpTask.getStatusListener() != null) {
            httpTask.getStatusListener().onSuccess();
        }
    }

    public String doPost(String urlStr, Map<String,String> httpHeadProperty, String params) throws Exception {
        return doCommonPost(urlStr,httpHeadProperty,params);
    }

    private String doCommonPost(String urlStr, Map<String,String> httpHeadProperty, String params) throws Exception {
        HttpURLConnection urlConnection = null;
        // 1.URL解码
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new Exception("URL解码出错！");
        }
        // 2.初始化HTTP链接
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new Exception("HTTP链接打开出错！");
        }
        // 3.配置HTTP链接参数
        try {
            // setConnectTimeout指与服务器建立连接的超时时间
            urlConnection.setConnectTimeout(HttpConfig.CONNECT_TIMEOUT);
            // setReadTimeout建立连接后如果指定时间内服务器没有返回数据，当做超时处理
            urlConnection.setReadTimeout(HttpConfig.CONNECT_TIMEOUT);
//            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//            urlConnection.addRequestProperty("Accept-Encoding", "max-age=0");
            for (String key : httpHeadProperty.keySet()) {
                urlConnection.setRequestProperty(key, httpHeadProperty.get(key));
            }
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            // 与后台全部用POST请求
            urlConnection.setRequestMethod("POST");
        } catch (Exception e) {
            throw new Exception("设置HTTP头出错！");
        }

        PrintWriter out = null;
        // 4.写出接口参数
        try {
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(urlConnection.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new Exception("HTTP输出缓冲区写入出错！");
        }
        // 5.链接到服务器
        try {
            urlConnection.connect();
        } catch (UnknownHostException e) {
            throw new Exception("解码HOST名字出错！");
        } catch (SocketTimeoutException e) {
            throw new Exception("链接超时！");
        } catch (IOException e) {
            throw new Exception("IO错误！");
        } catch (Exception e) {
            throw new Exception("未能链接到服务器，未知错误！");
        }
        // 6.得到服务器状态返回码
        int rspCode;
        try {
            rspCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            throw new Exception("获取HTTP返回码出错！");
        }
        // 7.判断返回码是否正确
        if (rspCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("不正确的HTTP返回码,"+rspCode+"！");
        }
        // 8.得到输入流,并且得到返回HTTP报文头参数
        InputStream responseIn = null;
        int totalLength = 0;
        try {
            responseIn = urlConnection.getInputStream();
            Map<String, List<String>> responseParam = urlConnection.getHeaderFields();
            // 得到长度;
            if (responseParam.get("Content-Length") != null
                    && responseParam.get("Content-Length") instanceof List
                    && responseParam.get("Content-Length").size() > 0) {
                String length = responseParam.get("Content-Length").get(0);
                try {
                    totalLength = Integer.parseInt(length);
                } catch (Exception e) {
                    totalLength = 0;
                }
            }
        } catch (IOException e) {
            Log.e(this.toString(),e.toString());
            if (responseIn != null) try {
                responseIn.close();
            } catch (IOException e1) {
                Log.e(this.toString(), e1.toString());
            }
            if (urlConnection != null) urlConnection.disconnect();
            throw new Exception("获取HTTP输入流出错！");
        }
        // 9.数据正常请求回来时才进行解析
        String key = urlConnection.getHeaderField("Content-Encoding");
        if (responseIn != null && key != null && "gzip".equals(key)) {
            try {
                responseIn = new MultiMemberGZIPInputStream(responseIn);
            } catch (IOException e) {
                Log.e(this.toString(),e.toString());
                if (responseIn != null) try {
                    responseIn.close();
                } catch (IOException e1) {
                    Log.e(this.toString(), e1.toString());
                }
                if (urlConnection != null) urlConnection.disconnect();
                throw new Exception("ZIP解码HTTP输入流出错！");
            }
        }
        // 10.正式接收数据
        byte[] buffer = null;
        ByteArrayOutputStream baos = null;
        try {
            final int BUF_SIZE = 1024;
            byte[] data = new byte[BUF_SIZE];
            int len = 0;
            int nowRcv = 0;
            long lastCallTime = System.currentTimeMillis();
            long nowCallTime = 0;
            baos = new ByteArrayOutputStream();
            try {
                while ((len = responseIn.read(data)) != -1) {
                    baos.write(data, 0, len);
                    if (totalLength > 0) {
                        nowRcv += len;
                        nowCallTime = System.currentTimeMillis();
                    }
                }
            } catch (EOFException e) {
                // 经测试遇到EOFException时，数据已读取完毕。
                Log.e(this.toString(),e.toString());
            }
            baos.flush();
            buffer = baos.toByteArray();
            baos.close();
        } catch (Exception e) {
            Log.e(this.toString(),e.toString());
            if (baos != null) try {
                baos.close();
            } catch (IOException e1) {
                Log.e(this.toString(), e1.toString());
            }
            if (responseIn != null) try {
                responseIn.close();
            } catch (IOException e1) {
                Log.e(this.toString(), e1.toString());
            }
            if (urlConnection != null) urlConnection.disconnect();
            throw new Exception("读取最终数据出错！");
        } finally {
            if (baos != null) try {
                baos.close();
            } catch (IOException e) {
                Log.e(this.toString(), e.toString());
            }
            if (responseIn != null) try {
                responseIn.close();
            } catch (IOException e) {
                Log.e(this.toString(), e.toString());
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
        // 11.转换字符串！
        String response = null;
        try {
            response = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception("组织数据成字符串出错！");
        }
        return response;
    }
}
