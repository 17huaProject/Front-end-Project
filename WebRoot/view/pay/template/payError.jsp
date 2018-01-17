<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>支付失败</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.4/css/bootstrap.css">
<link rel="stylesheet" href="../view/pay/skin/css/basic.css">
<link rel="stylesheet" href="../view/pay/skin/css/pay_fail.css">
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
        <h1>支付失败</h1>
    </div>
    <div class="share">
        <a href="#" class="h-share"></a>
    </div>
</header>

<div id="content" class="content">
    <ul class="tab">
    	<li>
        	<i class="pay-fail">支付失败</i>
            <h3>提示：${msg}[${retCode}]</h3>
        </li>
        <li>
            <a href="tel:400-088-2603">
            	客服热线：400-088-2603
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
