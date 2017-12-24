package serialPort.exceptions;

public class SerialPortParameterFailure extends Exception {
    @Override
    public String toString() {
        return "串口参数错误！";
    }
}
