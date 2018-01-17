<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>支付信息</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/css/bootstrap.css">
<link rel="stylesheet" href="../view/pay/skin/css/basic.css">
<link rel="stylesheet" href="../view/pay/skin/css/paymentinfor.css">
<link rel="stylesheet"  href="../view/pay/skin/css/mobiscroll.2.13.2.css">
</head>

<body>

<header id="header">
    <div class="back">
        <a href="#" class="h-return">
            <i></i>
            <span>返回</span>
        </a>
    </div>
    <div class="title">
        <h1>支付信息</h1>
    </div>
    <div class="share">
        <a href="#" class="h-share"></a>
    </div>
</header>

<div id="content" class="content">
	<ul class="tab2 tab1">
      <li>
          <label>订&nbsp;&nbsp;单&nbsp;&nbsp;号</label>
          <p class="p-1">${recordOrderInfo.order_id }</p>
          <div class="clear"></div>
      </li>
      <li class="nounderline">
          <label class="label-1">支付金额</label>
          <p class="p-strong">
            ${recordOrderInfo.paid_amount }元
          </p>
          <div class="clear"></div>
      </li>
    </ul>
    <form id="myForm" action="${ctx}/combo/jumpPage" method="post" >    
    <ul class="tab2 tab1">
      <li class="form form-small">
      	 <label>持&nbsp;&nbsp;卡&nbsp;&nbsp;人</label>
         <input type="text"  id="cardName" name="cardName" value="${record.realname }" disabled="disabled" class="text-right" maxlength="10" >
         <input type="hidden" id="orderSaveOilId" name="orderSaveOilId" value="${recordOrderInfo.order_id }"/>
         <input type="hidden" id="money" name="money" value="${recordOrderInfo.paid_amount }"/>
         <input type="hidden" id="idcard" name="idcard" value="${record.idcard }"/>
         <input type="hidden" id="redirect_uri" name="redirect_uri" value="${redirect_uri}"/>
         <div class="clear"></div>
      </li>
      <li class="form form-small" style="position:relative;">
            <label class="label-line">银&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;行</label>
            <select id="bank" name="bank">
                <option value="0">请选择银行</option> 
                <c:forEach items="${bankNames}" var="bank">
                	<option value="${bank.id}">${bank.name}</option> 
                </c:forEach>
            </select>
            <span class="caret"></span>
            <div class="clear"></div>
      </li>
      <li class="form form-small"  style="position:relative;">
          <label class="label-line">卡&nbsp;&nbsp;类&nbsp;&nbsp;型</label>
             <!-- <input type="text"  maxlength="16"  placeholder="借记卡"  >-->
          <select id="card" name="card">
                <option value="DC">借记卡</option> 
                <option value="CC">信用卡</option>
          </select>
          <span class="caret"></span>
          <div class="clear"></div>
      </li>
      <li class="nounderline form form-small">
          <label class="label-line">银行卡号</label>
          <input type="text"  maxlength="20" class="text-right" id="bankCord" name="bankCord"  placeholder="请输入您的银行卡号"  >
          <div class="clear"></div>
      </li>
    </ul>
	<button type="button"  id="save" class="sub-btn" >下一步</button> 
	</form>  
</div>
<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/js/bootstrap.js"></script>
<script src="../view/pay/skin/js/mobiscroll.2.13.2.js"></script>	
<script src="../view/pay/skin/js/select.js"></script>
<script src="../view/pay/skin/js/JCheck.js"></script>
<script src="../view/pay/skin/js/need-header.js"></script>
<script type="text/javascript">
	$('.u-radio').jCheckbox();	
	$(function(){
        $("#save").click(function(){
           var cardName = $("#cardName").val();
           var bank = $("#bank").val();
           var bankCord = $("#bankCord").val();
           if($.trim(cardName)==""){
               alert("持卡人不能为空");
               return false;
           }
           
           if(bank==0){
               alert("必须选择一个银行");
               return false;
           }
           if($.trim(bankCord)==""){
               alert("银行卡号不能为空");
               return false;
           }
           $("#myForm").submit();
        });
           
    });
</script>
</body>
</html>
