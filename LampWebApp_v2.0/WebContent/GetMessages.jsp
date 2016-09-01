<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="hzheng.mcumodule.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%! 
  /**
   * 日志句柄
   */
  private static Logger log = Logger.getLogger("GetMessages.jsp");

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
%>

<%
  //
  log.info("GetMsgs.jsp invoked.");
  //
  out.println("<result>");
  
  try {
    CommManager mManager; 
    mManager= CommManager.getManager("COM3");
    byte[] pMsg=mManager.getMessage();
    while (pMsg!=null) {
      if ((pMsg[2] == 0x02)&&(pMsg.length>6)) { //sensor report cmd
        String pTerminalId = byteToHexString(pMsg[0])+byteToHexString(pMsg[1]);
        int iRSSI = 256 - (pMsg[6]&0x0ff);
        String pRSSI = "-" + iRSSI;
        int iXValue = pMsg[3]&0x0ff;
        int iYValue = pMsg[4]&0x0ff;
        out.println("<msg xvalue='"+iXValue+"' yvalue='"+iYValue+"' nodeid='"+pTerminalId+"' rssi='"+pRSSI+"'>");
        out.println("<rawbytes>"+bytesToHexString(pMsg)+ "</rawbytes>");
        out.println("</msg>");
      }
      pMsg=mManager.getMessage();
    }    
  }catch (Exception e) {
    log.error(e.toString());
    out.println("<err>取消息错误：" + e.toString() +"</err>");
  }
  
  out.println("</result>");
%>
