package serialPort;

import serialPort.exceptions.*;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialEventSend {
    /**
     * 串口名称
     */
    private final static String PORT_PATH = "/dev/ttyS0";
    /**
     * 波特率
     */
    private final static int BAUD_RATE = 9600;
    /**
     * 数据位长度
     */
    private final static int DATA_BIT = 8;
    /**
     * 停止位长度
     */
    private final static int STOP_BIT = 1;
    /**
     * 奇偶校验
     */
    private final static char PARITY = 'N';

    private SerialPort serialPortClient = null;

    public void openPort() throws SerialPortParameterFailure, NoSuchPort, PortInUse {
        try {
            // 通过串口名称识别串口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(PORT_PATH);
            // 打开串口和相应的操作时间
            CommPort commPort = portIdentifier.open(PORT_PATH, 2000);
            // 具体的串口
            serialPortClient = (SerialPort)commPort;
            try {
                // 设置波特率等
                serialPortClient.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            }
            catch (UnsupportedCommOperationException e) {
                throw new SerialPortParameterFailure();
            }
        }
        catch (NoSuchPortException e1) {
            throw new NoSuchPort();
        }
        catch (PortInUseException e2) {
            throw new PortInUse();
        }
    }

    public void sendEvent(String event) throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
        OutputStream outputStream = null;
        try {
            outputStream = serialPortClient.getOutputStream();
            outputStream.write(event.getBytes());
            outputStream.flush();
        }
        catch (IOException e) {
            throw new SendDataToSerialPortFailure();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            }
            catch (IOException e) {
                throw new SerialPortOutputStreamCloseFailure();
            }
        }
    }

    String readEvent() throws ReadDataFromSerialPortFailure, SerialPortInputStreamCloseFailure {
        InputStream inputStream = null;
        byte[] bytes = null;
        try {
            inputStream = serialPortClient.getInputStream();
            int bufLen = inputStream.available();
            while (bufLen != 0) {
                bytes = new byte[bufLen];
                inputStream.read(bytes);
                bufLen = inputStream.available();
            }
        }
        catch (IOException e) {
            throw new ReadDataFromSerialPortFailure();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            }
            catch (IOException e) {
                throw new SerialPortInputStreamCloseFailure();
            }
        }
        if (bytes == null) return null;
        return new String(bytes);
    }

    public void closePort() {
        if (serialPortClient != null) {
            serialPortClient.close();
            serialPortClient = null;
        }
    }
}
