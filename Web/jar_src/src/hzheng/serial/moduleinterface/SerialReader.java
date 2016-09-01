/*+*********************************************************
* Filename: SerialReader.java
* �Ӵ��ڲ��϶������ݣ�����ָ���ĸ�ʽ����֡�綨�������ϲ�Ӧ�ô�����
*
* Modification:
*   creation  08.06.01  by Zheng Hui ������06.10.10 zigbeemodulemanager�����޸ģ�
*
* ����֡��ʽҪ����0x7e 0x7eΪ��־����֡�ָ�����־����ĵ�1���ֽ�Ϊ����ָʾ�ֽڡ�
***********************************************************-*/
package hzheng.serial.moduleinterface;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;



/**
 * �Ӵ��ڲ��϶������ݣ�����ָ���ĸ�ʽת��Ϊ�����࣬�����ϲ�Ӧ�ô�����
 */
public class SerialReader implements Runnable
{
  /**
   * ��־���
   */
  private static Logger log = Logger.getLogger("hzheng.serial.moduleinterface.SerialReader");

  /**
   * ���ڵ�����
   */
  private InputStream mInputStream;

  /**
   * �ϲ�Ӧ�õĻص�����
   */
  private UploadDataListenerI mListener;
  
  
  /**
   * un-sync flag
   */
  private boolean mUnsyncFlag = true; //=1, unsync

  /**
   * ���캯�����贫�봮�ڵ�InputStream���ϲ�Ӧ�õĻص�����
   */
  public SerialReader ( InputStream vInputStream, UploadDataListenerI vListener )
  {
      mInputStream = vInputStream;
      mListener = vListener;
  }

  /**
   * ���ݶ��뼰����
   */
   
  /**
   * ���ݸ�ʽ����0x7e 0x7eΪ��־����֡�ָ�����־����ĵ�1���ֽ�Ϊ����ָʾ�ֽڣ�ָʾ�����������ֽ���Ŀ��֡�������������256���ֽڡ�
   */
  private static int MAX_FRAME_SIZE = 256;
  public void run () {
    byte[] pFrame = new byte[MAX_FRAME_SIZE]; //�������������ֽ�
    int len, ret;
    
    try {
      while (true) { //until exception
        //�Ƚ�������֡ͬ�� ������FLAG��
        sync();
        //���볤��ָʾ�ֽ�
        len = mInputStream.read();
        pFrame[0] = (byte)len;
        //read next len bytes into buffer
        while ( len > 0 ) {
          ret=mInputStream.read(pFrame, pFrame[0] - len + 1, len );
          if (ret<0) {
            log.info("sub-thread quit.");
            break;
          }
        	len = len - ret;
        }
        HandlerFrame(pFrame);
        //
        if (Thread.interrupted()) {
          break;
        }
      }
    } catch ( IOException e ) {
      if (!Thread.interrupted()) {
        e.printStackTrace();
        log.error(e);
      }
      log.info("sub-thread quit.");
    }
  }

  /**
   * ֡ͬ��
   * flag = 0x7e 0x7e
   */
  private void sync() {
  	//read flag
    int iValue = -1;
    try {
      while (true ){
        iValue = mInputStream.read();
        if (iValue == -1)
          continue;
        //
        if (iValue == 0x7e) {
        	//read next flag byte
        	iValue = mInputStream.read();
        	if (iValue == 0x7e) {
            return;
          }
        }
      }
      
    } catch ( IOException e ) {
      e.printStackTrace();
      log.error(e);
    }  	
  }


  /**
   * ���ֽ�����ת��Ϊ����VO�࣬���ϲ�Ӧ�ô�����
   */
  private void HandlerFrame(byte[] vFrame) {
    //
    byte[] pNewFrame = new byte[vFrame[0]];
    for (int i=0;i<vFrame[0];i++) {
      pNewFrame[i]=vFrame[i+1];
    }
    mListener.handleData(pNewFrame);
  }
}