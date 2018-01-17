<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>支付信息</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/css/bootstrap.css">
<link rel="stylesheet" href="../view/pay/skin/css/basic.css">
<link rel="stylesheet" href="../view/pay/skin/css/paymentinfor.css">
<link rel="stylesheet"  href="../view/pay/skin/css/mobiscroll.2.13.2.css">
<link rel="stylesheet"  href="../view/pay/layer/skin/layer.css">
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
<div class="fakeloader"></div>
<div id="content" class="content">
    <ul class="tab2">
      <li>
          <label>${recordOrderInfo.productName }${recordOrderInfo.comboName }</label>
          <p class="p-strong">￥${recordOrderInfo.paid_amount }</p>
          <div class="clear"></div>
      </li>
      <li class="nounderline">
          <label>本次攒油（单位：升）</label>
              <p class="p-1">${oilNum}</p>
              <div class="clear"></div>
      </li>
    </ul>
   
   <h2>本次交易需要短信确认，验证码已发送至您的银行预留手机号 <strong>${record.save_mobile}</strong></h2>
    
    <ul class="tab1 tab2 form form-small">
      <!--<li>
          <form  class="form form-small">
              <label class="label-line">手机号</label>
              <input type="text"  maxlength="11"  placeholder="请输入银行预留手机号"  >
          </form>
          <div class="clear"></div>
      </li>-->
      
      <li class="nounderline form form-small" >
               <label class="label-line" >验&nbsp;&nbsp;证&nbsp;&nbsp;码</label>
               <input type="button" id="btn" style="margin-top: 0px;"  value="获取验证码" >
               <input type="text" value="" id="code"   placeholder="请输入验证码" maxlength="6" style=" width:45%; text-align:left;float: ">
               
          <div class="clear"></div>
      </li>
    </ul>
    <!-- 
    <label class="u-radio" data-name="radio1">
        <input name="radio1" type="checkbox" id="radio1">
        <i class="icon"></i>
        <span class="text">本人已阅读并同意<a href="#">《攒油宝用户协议》</a></span>
    </label>
     -->
    <form  id="inputForm" action="${ctx}/combo/confirmPayLkl" method="post">
    	<input type="hidden" id="verificationCode" name="verificationCode" value=""/>
    	<input type="hidden" id="orderSaveOilId" name="orderSaveOilId" value="${record.orderSaveOilId}"/>
    	<input type="hidden" id="orderAmount" name="orderAmount" value="${record.money}"/>
    	<input type="hidden" id="transactionId" name="transactionId" value="${transactionId}"/>
    	<input type="hidden" id="redirect_uri" name="redirect_uri" value="${redirect_uri}"/>
		<button type="button" id="save" class="sub-btn" >确认付款(${recordOrderInfo.paid_amount})</button> 
	</form>
</div>
<footer id="footer2">
    拉卡拉支付有限公司
</footer>
<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/js/bootstrap.js"></script>
<script src="../view/pay/skin/js/sendCode.js"></script>
<script src="../view/pay/skin/js/mobiscroll.2.13.2.js"></script>	
<script src="../view/pay/skin/js/select.js"></script>
<script src="../view/pay/skin/js/JCheck.js"></script>
<script src="../view/pay/layer/layer.js"></script>
<script src="../view/pay/skin/js/need-header.js"></script>
<script type="text/javascript">
	$('.u-radio').jCheckbox();	
	$("#btn").trigger("click");
	$(function(){
        $("#save").click(function(){
           var code = $("#code").val();
           alert(code);
           if($.trim(code)==""){
               alert("验证码不能为空");
               return false;
           }else{
        	   $("#verificationCode").val(code);
           }
           
           $("#inputForm").submit();
           layer.load(2);
           //此处演示关闭
           setTimeout(function(){
             layer.closeAll('loading');
           }, 10000); 
        });
        
        $("#btn").click(function(){
    		var orderSaveOilId = $("#orderSaveOilId").val();
    		var transactionId = $("#transactionId").val();
            $.ajax({
                type: "POST",
                url: "${ctx}/combo/sendOtp",
                data:{
                	orderSaveOilId : orderSaveOilId,
                	transactionId : transactionId
                },
                dataType: "json",
                success: function(json) {
    		    	alert(json.errmsg);
                }
            });
            return false;
         });
           
    });
	
	
</script>
</body>
</html>
