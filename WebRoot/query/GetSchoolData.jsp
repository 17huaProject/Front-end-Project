<%@ page language="java" import="java.util.*,java.io.*,java.net.*" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% 
   String path = request.getContextPath();
   String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

  String name = request.getParameter("name");
  String type = request.getParameter("type");
  String page = request.getParameter("p");
  String size = request.getParameter("s");
  if (name == null) {
     name = "";
  }
  if (type == null) {
     type = "";
  }
  if (page == null || "".equals(page)) {
     page = "0";
  }
  if (size == null || "".equals(size)) {
     size = "15";
  } 
  
  StringBuffer urlParam = new StringBuffer();
  urlParam.append("http://mbjy.nbedu.net.cn/api/InfoService/GetSchools?");
  urlParam.append("name="+URLEncoder.encode(name,"UTF-8"));
  urlParam.append("&type="+type);
  urlParam.append("&area=&business=&train=");
  urlParam.append("&pIndex="+page);
  urlParam.append("&pSize="+size);
   
   
    BufferedReader in = null;
    StringBuffer result = new StringBuffer();
    try {
    URL url = new URL(urlParam.toString());
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setUseCaches(false);
    con.setFollowRedirects(true);
    con.setConnectTimeout(6000);
    in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
    while (true) {
      String line = in.readLine();
      if (line == null) {
        break;
      }
      else {
    	  result.append(line);
      }
    }
    }catch(Exception e) {
    	e.printStackTrace();
    }finally {
    	if (in != null)
    	   in.close();
    }
    response.setContentType("text/html;charset=utf-8");
    out.print(result.toString());

%>
