package com.hhkj.talkdata.network.netlayer.base.common;

import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by guold on 2016/7/5.
 */
public class ObjectID {
    private final int timestamp;
    private final int machineIdentifier;
    private final short processIdentifier;
    private final int counter;
    private static final int MACHINE_IDENTIFIER;
    private static final short PROCESS_IDENTIFIER;
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static int createMachineIdentifier() {
        int machinePiece;
        try {
            StringBuilder t = new StringBuilder();
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)e.nextElement();
                t.append(ni.toString());
                byte[] mac = ni.getHardwareAddress();
                if(mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);

                    try {
                        t.append(bb.getChar());
                        t.append(bb.getChar());
                        t.append(bb.getChar());
                    } catch (BufferUnderflowException var7) {
                    }
                }
            }

            machinePiece = t.toString().hashCode();
        } catch (Throwable var8) {
            machinePiece = (new SecureRandom()).nextInt();
        }

        machinePiece &= 16777215;
        return machinePiece;
    }

    private static short createProcessIdentifier() {
        short processId = (short) android.os.Process.myPid();

        return processId;
    }

    private static int dateToTimestampSeconds(Date time) {
        return (int)(time.getTime() / 1000L);
    }

    public ObjectID(Date date, int counter) {
        this.timestamp = dateToTimestampSeconds(date);
        this.counter = counter;
        this.machineIdentifier = MACHINE_IDENTIFIER;
        this.processIdentifier = PROCESS_IDENTIFIER;
    }

    private static byte int3(int x) {
        return (byte)(x >> 24);
    }

    private static byte int2(int x) {
        return (byte)(x >> 16);
    }

    private static byte int1(int x) {
        return (byte)(x >> 8);
    }

    private static byte int0(int x) {
        return (byte)x;
    }

    private static byte short1(short x) {
        return (byte)(x >> 8);
    }

    private static byte short0(short x) {
        return (byte)x;
    }


    public byte[] toByteArray() {
        byte[] bytes = new byte[]{int3(this.timestamp), int2(this.timestamp), int1(this.timestamp), int0(this.timestamp), int2(this.machineIdentifier), int1(this.machineIdentifier), int0(this.machineIdentifier), short1(this.processIdentifier), short0(this.processIdentifier), int2(this.counter), int1(this.counter), int0(this.counter)};
        return bytes;
    }

    public String toHexString() {
        char[] chars = new char[24];
        int i = 0;
        byte[] var3 = this.toByteArray();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            chars[i++] = HEX_CHARS[b >> 4 & 15];
            chars[i++] = HEX_CHARS[b & 15];
        }

        return new String(chars);
    }

    static {
        try {
            MACHINE_IDENTIFIER = createMachineIdentifier();
            PROCESS_IDENTIFIER = createProcessIdentifier();
        } catch (Exception var1) {
            throw new RuntimeException(var1);
        }
    }
}
