import java.io.IOException;
import java.net.*;
import java.util.List;

public class NetEventSend {
    private final static String DEFAULT_SERVER_IP = "10.108.164.222";
    private final static int DEFAULT_SERVER_PORT = 5556;

    private List<String> events = null;
    private DatagramSocket client = null;
    private InetAddress serverAddr = null;

    private boolean sendEvents() {
        if (!initConnection()) {
            return false;
        }

        EventSimulation eventSimulation = new EventSimulation();
        eventSimulation.constructEvents();
        events = eventSimulation.getEvents();

        for (String event : events) {
            byte[] eventBytes = event.getBytes();
            DatagramPacket eventPacket = new DatagramPacket(eventBytes, eventBytes.length, serverAddr, DEFAULT_SERVER_PORT);
            try {
                client.send(eventPacket);
            }
            catch (IOException e) {
                System.err.println("数据包发送失败！" + e);
            }
        }
        client.close();
        return true;
    }

    private boolean initConnection() {
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

    private boolean sendEvent(String event) {
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

    public static void Main(String[] args) {
        NetEventSend netEventSend = new NetEventSend();
//        if (!netEventSend.sendEvents()) {
//            System.err.println("无法发送事件");
//        }
        EventSimulation eventSimulation = new EventSimulation();
        int eventAll = EventSimulation.getEventAll();
        for (int i = 0; i < eventAll; ++i) {
            int x = eventSimulation.getDynamicValue();
            int y = eventSimulation.getDynamicValue();
            String event = eventSimulation.getEvent(x, y);
            netEventSend.sendEvent(event);
        }
    }
}
