/*+*********************************************************
* Filename: UploadDataListenerI.java
* �����ϲ�Ӧ�û�ȡ�Ӵ��нӿڽ��յ������ݵĽӿڡ�
*
* Modification:
*   creation  08.06.01  by Zheng Hui
*
***********************************************************-*/
package hzheng.serial.moduleinterface;


public interface UploadDataListenerI {
  /**
   * ��������
   */
  public void handleData(byte[] vData);

}