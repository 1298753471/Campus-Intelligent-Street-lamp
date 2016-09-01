/*+*********************************************************
* Filename: CommController.java
* ͨ��������WSNģ��ͨ�ţ�ʵ�ִ������ݵ�֡�綨�����ϲ�Ӧ��
* �ṩ�����ڴ��нӿ�Э���ʽ��Ӧ�ýӿڡ�
*
* Modification:
*   creation  08.06.01  by Zheng Hui
*
***********************************************************-*/
package hzheng.serial.moduleinterface;

import org.apache.log4j.Logger;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * �������ж˿ڵĶ�д��Singleton�ࡣ
 * �Ե������̶߳����Ըö��̵߳����ϲ�Ӧ�õĻص������������ݴ�����
 * ����ϲ�Ӧ�õĻص���������������
 */
public class CommController {

  /**
   * ��־���
   */
  private static Logger log = Logger.getLogger("hzheng.serial.moduleinterface.CommController");

  /**
   * �����Ψһʵ��
   */
  private static CommController mController = null;


  /**
   * ����ʵ��
   */
  private SerialPort mSerialPort = null;
  
  /**
   * �������߳�
   */
  private Thread mSubThread = null;
  

  /**
   * ���캯������ֹӦ��ֱ�Ӵ�������ʵ��
   */
  private CommController()
  {
  }

  /**
   * Ӧ��ͨ�����������ʵ��
   */
  public static CommController getController() {
    if (mController == null)
      mController = new CommController();

    return mController;
  }


  /**
   * ����COM�ڣ��½����ڶ��߳�
   */
  public void connect ( String vPortName, UploadDataListenerI vListener ) throws Exception  {
      CommPortIdentifier pPortIdentifier = CommPortIdentifier.getPortIdentifier(vPortName);
      if ( pPortIdentifier.isCurrentlyOwned() )
      {
          log.error("Port is currently in use");
      }
      else
      {
          CommPort pCommPort = pPortIdentifier.open(this.getClass().getName(),2000);

          if ( pCommPort instanceof SerialPort )
          {
              mSerialPort = (SerialPort) pCommPort;
              //mSerialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
              mSerialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

              InputStream in = mSerialPort.getInputStream();
              
              mSubThread=new Thread(new SerialReader(in, vListener));

              mSubThread.start();

          }
          else
          {
              log.error("specified port is not serial ports.");
          }
      }
  }


  /**
   * ��������
   */
  public void send ( byte[] vBytes) throws Exception  {
    OutputStream pOut = mSerialPort.getOutputStream();

    pOut.write(vBytes);
    
    //
    String sendString = bytesToHexString(vBytes);
    log.info("data sent: " + sendString);

  }
  /**
   * �˳�
   */
  public void close () {
    mSerialPort.close();
    mSubThread.interrupt();
  }

  /**
   * convert bytes to hex string
   */
  private String bytesToHexString(byte[] vBytes) {
    String s = new String("");
    if ((vBytes == null) || (vBytes.length == 0)) {
      return s;
    }
    for (int i=0;i<vBytes.length;i++) {
      s = s + " " + byteToHexString(vBytes[i]);
    }
    return s;
  }
  /**
   * convert byte to hex string
   */
  private String byteToHexString(byte vByte) {
    String s = Integer.toHexString(vByte&0xff);
    if (s.length() == 1)
      s = "0" + s;
      
    return s;
  }


  //for test
  private static class App implements UploadDataListenerI {
    public void handleData(byte[] vData) {
      System.out.println(vData);
    }
  }

  public static void main ( String[] args )
  {
    if (args.length != 1) {
      System.out.println("Usage: java CommController <COM port name>");
      return;
    }

    App pApp = new App();
    try {
        CommController.getController().connect(args[0], pApp);
    } catch ( Exception e ) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
      while (true) {
        Thread.currentThread().sleep(1000);
      }
    } catch ( Exception e ) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
}