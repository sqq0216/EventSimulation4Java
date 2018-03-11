import Net.NetEventSend;
import serialPort.SerialEventSend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EventSimulation {
    public static int getEventAll() {
        return EVENT_ALL;
    }

    /**
     * 总共模拟多少个事件
     */
    private final static int EVENT_ALL = 1_0;
    /**
     * 每个事件多长
     */
    private final static int EVENT_LENGTH = 100;
    /**
     * 关键事件产生概率
     */
    private final static double IMPORTANT_EVENT_PRO = 0.7;
    /**
     * 事件发送间隔(ns)
     */
    private final static int SLEEP_TIME = 1_000;
    /**
     * 动态生成事件的编号
     */
    private int dynamicEventNum = 1;

    /**
     * 关键事件名称列表
     */
    private final static List<String> IMPORTANT_EVENT_NAME_LIST = new ArrayList<>(Arrays.asList(
            "increase",
            "decrease"
    ));

    /**
     * 根据概率动态生成事件名称
     * @return
     */
    private String getEventName() {
        if (Math.random() > IMPORTANT_EVENT_PRO) {
            return "notImportantEventName";
        }
        else {
            return IMPORTANT_EVENT_NAME_LIST.get((int)(Math.random() * IMPORTANT_EVENT_NAME_LIST.size()));
        }
    }

    /**
     * 静态构造的事件列表
     */
    private List<String> staticEvents = new ArrayList<>(EVENT_ALL);
    /**
     * 构造事件
     */
    private void constructStaticEvents() {
        String eventHead, eventTail, event;
        for (int i = 1; i <= EVENT_ALL; ++i) {
            eventHead = "<xml type=\"event\" name=\"" + getEventName() + "\" num=\"" + String.valueOf(i) + "\" attr=\"";
            eventTail = "\"><x>" + String.valueOf(i % 100) + "</x><y>" + String.valueOf(i % 50) + "</y></xml>";
            StringBuilder eventSB = new StringBuilder();
            for (int j = 0; j < EVENT_LENGTH - eventHead.length() - eventTail.length(); ++j) {
                eventSB.append("*");
            }
            event = eventHead + eventSB.toString() + eventTail;
            staticEvents.add(event);
        }
    }

    /**
     * 动态生成随机变量值
     * @return
     */
    private int getDynamicValue() {
        return (int)(Math.random() * 100);
    }

    /**
     * 动态生成事件
     * @return
     */
    private String increase(int x, int y) {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String eventHead, eventTail, event;

        eventHead = "<xml type=\"event\" name=\"increase\" num=\"" + String.valueOf(dynamicEventNum++) + "\" attr=\"";
        eventTail = "\"><x>" + String.valueOf(x) + "</x><y>" + String.valueOf(y) + "</y></xml>";
        StringBuilder eventSB = new StringBuilder();
        for (int j = 0; j < EVENT_LENGTH - eventHead.length() - eventTail.length(); ++j) {
            eventSB.append("*");
        }

        event = eventHead + eventSB.toString() + eventTail;
        return event;
    }

    private String decrease(int x, int y) {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String eventHead, eventTail, event;

        eventHead = "<xml type=\"event\" name=\"decrease\" num=\"" + String.valueOf(dynamicEventNum++) + "\" attr=\"";
        eventTail = "\"><x>" + String.valueOf(x) + "</x><y>" + String.valueOf(y) + "</y></xml>";
        StringBuilder eventSB = new StringBuilder();
        for (int j = 0; j < EVENT_LENGTH - eventHead.length() - eventTail.length(); ++j) {
            eventSB.append("*");
        }

        event = eventHead + eventSB.toString() + eventTail;
        return event;
    }

    private String unimportant(int x, int y) {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String eventHead, eventTail, event;

        eventHead = "<xml type=\"event\" name=\"unimportant\" num=\"" + String.valueOf(dynamicEventNum++) + "\" attr=\"";
        eventTail = "\"><x>" + String.valueOf(x) + "</x><y>" + String.valueOf(y) + "</y></xml>";
        StringBuilder eventSB = new StringBuilder();
        for (int j = 0; j < EVENT_LENGTH - eventHead.length() - eventTail.length(); ++j) {
            eventSB.append("*");
        }

        event = eventHead + eventSB.toString() + eventTail;
        return event;
    }


    public static void main(String[] args) {
        EventSimulation eventSimulation = new EventSimulation();

        NetEventSend netEventSend = new NetEventSend();
        if (!netEventSend.initConnection()) {
            return ;
        }

        SerialEventSend serialEventSend = new SerialEventSend();
        try {
            serialEventSend.openPort();
        }
        catch (Exception e) {
            System.err.println(e);
            return ;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入指令（0 继续，1退出）:");
            String cmd = scanner.nextLine();
            if ("1".equals(cmd)) {
                break;
            }
            else if (!"0".equals(cmd)) {
                System.err.println("无法识别指令" + cmd);
                continue;
            }

            System.out.println("继续发送长度为" + EVENT_LENGTH + "数量为" + EVENT_ALL + "的事件");
            for (int i = 0; i < EVENT_ALL; ++i) {
                int x = eventSimulation.getDynamicValue();
                int y = eventSimulation.getDynamicValue();
                String event = null;
                if (Math.random() > IMPORTANT_EVENT_PRO) {
                    event = eventSimulation.unimportant(x, y);
                }
                else if (Math.random() > 0.5) {
                    event = eventSimulation.increase(x, y);
                }
                else {
                    event = eventSimulation.decrease(x, y);
                }
//                String event = eventSimulation.getEvent(x, y);
                System.out.println("动态构造的事件为:" + event);

                // 每10个事件中取1个发往串口
                netEventSend.sendEvent(event);
                if (i % 2 == 0) {
                    try {
                        serialEventSend.sendEvent(event);
                    }
                    catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
        }
        netEventSend.closeConnection();
        serialEventSend.closePort();
    }
}
