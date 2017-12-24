package serialPort.exceptions;

public class ReadDataFromSerialPortFailure extends Exception {
    @Override
    public String toString() {
        return "从串口读取数据失败！";
    }
}
