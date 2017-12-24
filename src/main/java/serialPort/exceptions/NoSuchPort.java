package serialPort.exceptions;

public class NoSuchPort extends Exception {
    @Override
    public String toString() {
        return "无此串口！";
    }
}
