package serialPort.exceptions;

public class SerialPortOutputStreamCloseFailure extends Exception {
    @Override
    public String toString() {
        return "串口输出流关闭失败！";
    }
}
