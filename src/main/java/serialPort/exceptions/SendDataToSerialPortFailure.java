package serialPort.exceptions;

public class SendDataToSerialPortFailure extends Exception {
    @Override
    public String toString() {
        return "往串口发送数据失败！";
    }
}
