<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="dayoo.ClickStat.util.CSUtil"%>
<%
boolean isMultipart = ServletFileUpload.isMultipartContent(request);
if(isMultipart){
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	List items = upload.parseRequest(request);
	Iterator iter = items.iterator();
	int rv = CSUtil.revoverFromFile(iter);
%>
<H1>恢复<%=rv %>条记录</H1>
<%}%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
function start(){
	
}
</script>
  </head>
  
  <body>
<center>
<form id="form1" name="form1" enctype="multipart/form-data" method="post" action="importFromXML.jsp">
    <label>请选择stat.xml文件:
      <input type="file" name="stat" />
    </label>
	<input type="submit" name="button" onclick="start()" value="导入" />
</form>
</center>
  </body>
</html>
