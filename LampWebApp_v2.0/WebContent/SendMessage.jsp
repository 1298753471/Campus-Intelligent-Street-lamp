<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="hzheng.mcumodule.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%! 
  private static Logger log = Logger.getLogger("SendMessage.jsp");
%>


<%
  
  //get msg from parrameter
  String sCmd = request.getParameter("cmd");
  String sOption = request.getParameter("option");
  byte[] pMsgBytes = new byte[2];
  try {
    pMsgBytes[0]=(byte)Integer.parseInt(sCmd);
    pMsgBytes[1]=(byte)Integer.parseInt(sOption);
  }catch (Exception e) {
  }
  //
  log.info("send msg: cmd=" + sCmd + ";option=" + sOption);
  //
  out.println("<result>");
  try {
    CommManager mManager; 
    mManager= CommManager.getManager("COM3");
    mManager.sendMsg(pMsgBytes);
    out.println("<status>success</status>");
    
  }catch (Exception e) {
    out.println("<status>failed:" + e.toString() +"</status>");
  }
  out.println("</result>");
%>
