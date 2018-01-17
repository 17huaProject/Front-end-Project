<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>支付信息</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/css/bootstrap.css">
<link rel="stylesheet" href="../view/pay/skin/css/basic.css">
<link rel="stylesheet" href="../view/pay/skin/css/paymentinfordetail.css">
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
    <form id="myForm" action="${ctx}/combo/confirmPayLkl" method="post">
    	<input type="hidden" id="transactionId" name="transactionId" value="${transactionId }"/>
    	<input type="hidden" id="h_cardType" name="cardType" value="${card}"/>
    	<input type="hidden" id="orderSaveOilId" name="orderSaveOilId" value="${recordOrderInfo.order_id }"/>
    	<input type="hidden" id="orderAmount" name="orderAmount" value="${recordOrderInfo.paid_amount }"/>
    	<input type="hidden" id="bankCord" name="bankCord" value="${bankCord}"/>
    	<input type="hidden" id="bank" name="bank" value="${bank}"/>
    	<input type="hidden" id="redirect_uri" name="redirect_uri" value="${redirect_uri}"/>
    	<c:choose>
    	<c:when test="${card == 'CC' }">
    		<ul class="tab2 tab1 form form-small" >
		      <li>
		              <label class="label-line">CVV码</label>
		              <input type="text"  maxlength="4" class="text-right" id="cvv" name="cvv"  placeholder="请输入CVV码"  >
		          <div class="clear"></div>
		      </li>
		      <li class="nounderline form form-small"  style="position:relative;">
		                <label style="margin:6px 0 0 0;" >卡有效日期</label>
		            	<input   placeholder="请输入信用卡有效日期" id="effectiveDate" name="effectiveDate" class="demo-test-date" />
		            <span class="caret"></span>
		            <div class="clear"></div>
		      </li>
		    </ul>
    	</c:when>
    </c:choose>
    
    
    <ul class="tab2">
      <li>
          <label>姓名</label>
          <p class="p-strong">${likeCardName}</p>
          <div class="clear"></div>
      </li>
      <li class="nounderline">
          <label>身份证</label>
          <p class="p-strong">${likeIdcard}<input type="hidden" id="idcard" name="idcard" value="${idcard}"/></p>
          <div class="clear"></div>
      </li>
    </ul>
    
    
    <ul class="tab1 tab2 form form-small">
      <li>
              <label class="label-line">手机号</label>
              <input type="text"  maxlength="11" class="text-right" id="phone" name="phone"  placeholder="请输入银行预留手机号"  >
          <div class="clear"></div>
      </li>
      <li class="nounderline form form-small">
               <label class="label-line">验&nbsp;&nbsp;证&nbsp;&nbsp;码</label>
               <input type="button" id="btn"  value="获取验证码" >
               <input type="text" placeholder="请输入验证码" class="text-right" maxlength="6" id="verificationCode" name="verificationCode" style=" width:45%; text-align:left; float:left;">
               
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
	<button type="button"  id="save" class="sub-btn" >确认付款(${recordOrderInfo.paid_amount})</button>
    </form>
       
</div>
<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/js/bootstrap.js"></script>
<!-- <script src="../view/pay/skin/js/sendCode.js"></script> -->
<script src="../view/pay/skin/js/mobiscroll.2.13.2.js"></script>	
<script src="../view/pay/skin/js/select.js"></script>
<script src="../view/pay/skin/js/JCheck.js"></script>
<script src="../view/pay/layer/layer.js"></script>
<script src="../view/pay/skin/js/need-header.js"></script>
<script type="text/javascript">
	$('.u-radio').jCheckbox();	
	
	$(function(){
		$("#bank_dummy").remove();
        $("#save").click(function(){
           var h_cardType = $("#h_cardType").val();
           if(h_cardType == 'cc'){
        	   var cvv = $("#cvv").val();
        	   var effectiveDate = $("#effectiveDate").val();
        	   if($.trim(cvv)==""){
        		   alert("CVV号不能为空");
                   return false;
        	   }
        	   
        	   if($.trim(effectiveDate)==""){
        		   alert("请选择一个有效日期");
                   return false;
        	   }
           }
           var phone = $("#phone").val();
           if($.trim(phone)==""){
               alert("预留手机号码不能为空");
               return false;
           }
           var verificationCode = $("#verificationCode").val();
           if($.trim(verificationCode)==""){
               alert("验证码不能为空");
               return false;
           }
           $("#myForm").submit();
           layer.load(2);
           //此处演示关闭
           setTimeout(function(){
             layer.closeAll('loading');
           }, 10000); 
        });
        
        
        $("#btn").click(function(){
        	var transactionId = $("#transactionId").val();
    		var phone = $("#phone").val();
            if(phone == ''){
                alert("请输入手机号码");
                return false;
            }else{
                if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
                    alert("手机号码有误，请重填");
                    return false;
                }
            }
            time(this); 
            var url = '';
    		if(transactionId == '')url = '${ctx}/combo/submitOrder';
    		else url = '${ctx}/combo/sendOtp';
            $.ajax({
                type: "POST",
                url: url,
                data:{
                	phone : phone,
                	bank : $("#bank").val(),
                	idcard : $("#idcard").val(),
                	cardType : $("#h_cardType").val(),
                	cvv : $("#cvv").val(),
                	effectiveDate : $("#effectiveDate").val(),
                	orderSaveOilId : $("#orderSaveOilId").val(),
                	orderAmount : $("#orderAmount").val(),
                	bankCord : $("#bankCord").val(),
                	transactionId:transactionId
                },
                dataType: "json",
                success: function(json) {
                	/* $.each(json.record,function(key,values){ 
                		if (typeof(values.transactionId) != "undefined")
                		{
                			$("#transactionId").val(values.transactionId);
                		}   
                	 }); */
                	if(transactionId == ''){
                		alert(json.record.msg);
                		$("#transactionId").val(json.record.transactionId);
                	}else{
                		alert(json.errmsg);
                	}  
                }
            });
        });
    });
	
	
	var wait=60;
	function time(o) {
		if (wait == 0) {
		  o.removeAttribute("disabled",false);
		  o.style.backgroundColor = '#e73828'; 	
		  o.value="获取验证码";
		  wait = 60;
		}else {
		  o.setAttribute("disabled", true);
		  o.style.backgroundColor = '#ccc'; 
		  o.value="已发送(" + wait + "s)";
		  wait--;
		  setTimeout(function() {
			  time(o)
		  },1000)
		}
	  }
	  
	  
</script>

</body>
</html>
