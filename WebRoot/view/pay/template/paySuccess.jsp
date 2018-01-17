<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>支付成功</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/css/bootstrap.css">
<link rel="stylesheet" href="../view/pay/skin/css/basic.css">
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
        <h1>支付成功</h1>
    </div>
    <div class="share">
        <a href="#" class="h-share"></a>
    </div>
</header>

<div id="content" class="content">
    <ul class="tab">
    	<li>
        	<i class="pay-success">支付成功</i>
        </li>
        <li>
        	<i class="service-code">及时获取油价调整信息，请添加您的专属客服：</i>
            <img class="qr_code" src="../view/pay/skin/images/erweima.png">
        </li>
        <li class="nounderline">
        	<a href="#">
            	<i class="recommend">推荐给我的小伙伴，一起拿50元奖励！</i>
            </a>
        </li>
        <li class="nounderline">
        	<a href="${redirect_uri}">
            	返回订单
            </a>
        </li>
    </div>  
</div>

<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/js/bootstrap.js"></script>
<script src="../view/pay/skin/js/need-header.js"></script>
</body>
</html>
