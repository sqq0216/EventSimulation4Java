import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventSimulation {
    public static int getEventAll() {
        return EVENT_ALL;
    }

    /**
     * 总共模拟多少个事件
     */
    private final static int EVENT_ALL = 1_000;
    /**
     * 每个事件多长
     */
    private final static int EVENT_LENGTH = 100;
    /**
     * 关键事件产生概率
     */
    private final static double IMPORTANT_EVENT_PRO = 0.5;
    /**
     * 动态生成事件的编号
     */
    private int dynamicEventNum = 1;

    /**
     * 关键事件名称列表
     */
    private final static List<String> IMPORTANT_EVENT_NAME_LIST = new ArrayList<>(Arrays.asList(
            "increase"
    ));

    public List<String> getEvents() {
        return events;
    }

    private String getEventName() {
        if (Math.random() > IMPORTANT_EVENT_PRO) {
            return "notImportantEventName";
        }
        else {
            return IMPORTANT_EVENT_NAME_LIST.get((int)(Math.random() * IMPORTANT_EVENT_NAME_LIST.size()));
        }
    }

    /**
     * 事件列表
     */
    private List<String> events = new ArrayList<>(EVENT_ALL);
    /**
     * 构造事件
     */
    public void constructEvents() {
        String eventHead, eventTail, event;
        for (int i = 1; i <= EVENT_ALL; ++i) {
            eventHead = "<xml type=\"event\" name=\"" + getEventName() + "\" num=\"" + String.valueOf(i) + "\" attr=\"";
            eventTail = "\"><x>" + String.valueOf(i % 100) + "</x><y>" + String.valueOf(i % 50) + "</y></xml>";
            StringBuilder eventSB = new StringBuilder();
            for (int j = 0; j < EVENT_LENGTH - eventHead.length() - eventTail.length(); ++j) {
                eventSB.append("*");
            }
            event = eventHead + eventSB.toString() + eventTail;
            events.add(event);
        }
    }

    /**
     * 根据关键事件概率生成0或1
     * @return
     */
    private String getEventImportant() {
        if (Math.random() > IMPORTANT_EVENT_PRO) return "0";
        else return "1";
    }

    public int getDynamicValue() {
        return (int)(Math.random() * 100);
    }

    /**
     * 动态生成事件
     * @return
     */
    public String getEvent(int x, int y) {
        String eventHead, eventTail, event;

        eventHead = "<xml type=\"event\" name=\"" + getEventName() + "\" num=\"" + String.valueOf(dynamicEventNum++) + "\" attr=\"";
        eventTail = "\"><x>" + String.valueOf(x) + "</x><y>" + String.valueOf(y) + "</y></xml>";
        StringBuilder eventSB = new StringBuilder();
        for (int j = 0; j < EVENT_LENGTH - eventHead.length() - eventTail.length(); ++j) {
            eventSB.append("*");
        }

        event = eventHead + eventSB.toString() + eventTail;
        return event;
    }

}
