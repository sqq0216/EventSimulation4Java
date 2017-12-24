package serialPort.exceptions;

public class PortInUse extends Exception {
    @Override
    public String toString() {
        return "串口正在被使用中！";
    }
}
