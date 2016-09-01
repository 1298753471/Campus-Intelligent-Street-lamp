/*+*********************************************************
* Filename: CommManager.java
* 在Web页面与串口之间转发消息。
*
* Modification:
*   creation  08.06.01  by Zheng Hui
*   2010.10.24, hzheng, modify to suit messurement application
*   2010.12.08 modified from CommManager
*   2011.04.17 modified to fit MCU framework
***********************************************************-*/
package hzheng.mcumodule;

import java.util.Vector;
import org.apache.log4j.Logger;


import hzheng.serial.moduleinterface.CommController;
import hzheng.serial.moduleinterface.UploadDataListenerI;

/**
 * 串口消息转发管理
 */
public class CommManager implements UploadDataListenerI {

  /**
   * 日志句柄
   */
  private static Logger log = Logger.getLogger("hzheng.mcumodule.CommManager");

  /**
   * 本类的唯一实例
   */
  private static CommManager mManager = null;

  /**
   * 接收到的消息分组列表
   */
  private Vector<byte[]> mMessageList = null;

  /**
   * CommController的唯一实例
   */
  private static CommController mCommController = null;

  /**
   * 监听串口端口
   */
  private static String mComPort = null;
  
  
  private static int MAX_BUFFER_SIZE = 100;

  /**
   * 构造函数。防止应用直接创建本类实例
   */
  private CommManager() {
    mMessageList = new Vector<byte[]>();
  }


  /**
   * 应用通过本方法获得实例
   */
  public static CommManager getManager(String vComPort) {
    //log.info("getManager invoked." +vComPort);
    if (mManager == null) {
      mManager = new CommManager();
    }
    if (mCommController == null) {
      //启动串口监听
      try {
        CommController.getController().connect(vComPort, mManager);
        mCommController = CommController.getController();
        mComPort = vComPort;
        
        log.info("Comm Manager init success.");
      } catch ( Exception e ) {
        log.error(e);
        mManager = null;
      }
    }else if (!mComPort.equals(vComPort)) {
      //mCommController.free();
      mCommController = null;
      try {
        CommController.getController().connect(vComPort, mManager);
        mCommController = CommController.getController();
        mComPort = vComPort;
        log.info("Comm Manager re-init success.");
      } catch ( Exception e ) {
        log.error(e);
        mManager = null;
      }
    }

    return mManager;
  }

  /**
   * 应用通过本方法获取接收到的消息分组
   */
  public byte[] getMessage() {
    int iSize = mMessageList.size();
    byte[] pRetMsg=null;
    if (iSize>0) {
      pRetMsg = mMessageList.get(0);
      
      synchronized (mMessageList) {
        mMessageList.remove(0);
      }
    }
    return pRetMsg;
  }
  /**
   * 应用通过本方法获取接收到的消息分组（字符串格式）
   */
  public String getMessageString() {
    int iSize = mMessageList.size();
    String pRetMsg=null;
    if (iSize>0) {
      pRetMsg = new String(mMessageList.get(0));
      
      synchronized (mMessageList) {
        mMessageList.remove(0);
      }
    }
    return pRetMsg;
  }
  /**
   * 应用通过本方法获取接收到的消息分组（16进制字符串格式）
   */
  public String getMessageHexString() {
    log.info("getMessageHexString invoked.");
    int iSize = mMessageList.size();
    String pRetMsg=null;
    if (iSize>0) {
      pRetMsg = bytesToHexString(mMessageList.get(0));
      
      synchronized (mMessageList) {
        mMessageList.remove(0);
      }
    }
    return pRetMsg;
  }


  /**
   * 收到上传数据时的处理
   */
  public void handleData(byte[] vData) {
    log.info("Recieved data: " + bytesToHexString(vData));
    
    synchronized (mMessageList) {
      mMessageList.addElement(vData);
      if (mMessageList.size()>MAX_BUFFER_SIZE) {
        mMessageList.remove(0);
      }
    }
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
   * 发送分组
   */
  public void sendMsg(byte[] vMsgData) {
    log.info("sendMsg invoked:"+bytesToHexString(vMsgData));
    byte[] pSendBuffer = new byte[vMsgData.length+3];
    pSendBuffer[0]=0x7e;
    pSendBuffer[1]=0x7e;
    pSendBuffer[2]=(byte)((vMsgData.length)&0xff);
    for (int i=0;i<vMsgData.length;i++) {
      pSendBuffer[i+3]=vMsgData[i];
    }
    //
    try {
      CommController.getController().send(pSendBuffer);
    } catch ( Exception e ) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
  }


  /**
   * 程序入口
   */
  public static void main ( String[] args )  {
    if (args.length != 1) {
      System.out.println("Usage: java hzheng.mcumodule.CommManager <COM port name>");
      return;
    }

    CommManager pManager = CommManager.getManager(args[0]);
    
    if (pManager == null) {
      System.out.println("init failed.");
      System.exit(-1);
    }

    System.out.println("init success.");
    
    while(true) {
      try {
        Thread.sleep(1000);
      }catch (Exception e) {
        System.out.println("quit.");
        break;
      }
    }

  }
}
