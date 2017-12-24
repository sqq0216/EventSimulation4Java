package serialPort.exceptions;

public class SerialPortInputStreamCloseFailure extends Exception {
    @Override
    public String toString() {
        return "串口输入流关闭失败！";
    }
}
