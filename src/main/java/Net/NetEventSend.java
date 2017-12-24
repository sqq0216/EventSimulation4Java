package Net;

import java.io.IOException;
import java.net.*;
import java.util.List;

public class NetEventSend {
    private final static String DEFAULT_SERVER_IP = "10.108.164.222";
    private final static int DEFAULT_SERVER_PORT = 5556;

    private DatagramSocket client = null;
    private InetAddress serverAddr = null;

    boolean sendEvents(List<String> events) {
        for (String event : events) {
            if (!sendEvent(event)) {
                return false;
            }
        }
        return true;
    }

    public boolean initConnection() {
        try {
            client = new DatagramSocket();
        }
        catch (SocketException e) {
            System.err.println("无法创建客户端套接字" + e);
            return false;
        }
        try {
            serverAddr = InetAddress.getByName(DEFAULT_SERVER_IP);
        }
        catch (UnknownHostException e) {
            System.err.println("无效的服务端IP地址" + e);
            return false;
        }
        return true;
    }

    public void closeConnection() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    public boolean sendEvent(String event) {
        if (client == null) {
            System.err.println("连接尚未建立！");
            return false;
        }
        byte[] eventBytes = event.getBytes();
        DatagramPacket eventPacket = new DatagramPacket(eventBytes, eventBytes.length, serverAddr, DEFAULT_SERVER_PORT);
        try {
            client.send(eventPacket);
        }
        catch (IOException e) {
            System.err.println("数据包发送失败！" + e);
            return false;
        }
        return true;
    }
}
