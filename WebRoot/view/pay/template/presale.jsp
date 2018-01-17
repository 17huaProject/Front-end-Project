<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 搜索
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>欢迎关注攒油宝</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/jquery-mobile/1.4.5/jquery.mobile.min.css">
    <link rel="stylesheet" href="${ctx}/view/pay/skin/css/jquery.mobile-1.4.5.minadd.css">
	<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/jquery-mobile/1.4.5/jquery.mobile.min.js"></script>
    <script src="${ctx}/view/pay/skin/js/hm.js"></script>
    <script src="${ctx}/view/pay/skin/js/presale.js"></script>
<!--     <script type="text/javascript">
    var error = '${msg}';
     function show(){ 
    	
    	 if (error) {
    		 alert(error);
    	 }
     }
     $(document).ready(function(){
    	 if (error) {
    		 alert("-----"+error);
    	 }
    	
      })
    </script>  --> 
</head>

<body style="background-color: rgb(239, 239, 239);" class="ui-mobile-viewport ui-overlay-a">
	<div data-role="page" tabindex="0" class="ui-page ui-page-theme-a ui-page-active" style="min-height: 925px;">
    	<div>
    	    <!-- 
            <div data-role="header" style="background-color:#00113C" role="banner" class="ui-header ui-bar-inherit">
                <h1 style="color:#FFFFFF" class="ui-title" role="heading" aria-level="1">攒油宝 </h1>
            </div>  -->
            <div class="ui-content" id="FormPanel1">
                <form id="FormA" enctype="multipart/form-data" method="post" action="${ctx}/presale/add">
                    <input type="hidden" id="lockToken" name="lockToken" value="${lockToken}"/>
                    <div>
                        <label for="form1">真实姓名*</label>
                        <div class="ui-input-text ui-body-inherit ui-corner-all ui-shadow-inset">
                        	<input type="text" id="name" name="name" placeholder="请填写您的真实姓名" value="">
                        </div>
                    </div>
                    <div>
                        <label for="form2">性别*</label>
                        <div class="ui-select ui-body-inherit ui-corner-all ui-shadow-inset">
                        	<div id="form2-button" style="position:relative;">
                            	<span style="position:absolute; left:0; top:0;">男</span>
                                <select id="sex" name="sex" style="position:absolute; left:0; top:0;">
                                    <option value="1">男</option>
                                    <option value="2">女</option>
                                </select>
                             </div>
                        </div>
                    </div>
                    <div>
                        <label for="form3">本人电话*</label>
                        <div class="ui-input-text ui-body-inherit ui-corner-all ui-shadow-inset">
                        	<input type="text" id="mobile" name="mobile" placeholder="请填写你的手机号码" value="">
                        </div>
                    </div>
                    <div>
                        <label for="form4">电子邮箱*</label>
                        <div class="ui-input-text ui-body-inherit ui-corner-all ui-shadow-inset">
                        	<input type="text" id="email" name="email" placeholder="请填写您常用的电子邮箱" value="">
                        </div>
                    </div>
                    <div id="form7div">
                        <label for="form7">已购车品牌*</label>
                        <div class="ui-input-text ui-body-inherit ui-corner-all ui-shadow-inset">
                        	<input type="text" id="car_type" name="car_type" placeholder="请填写例如：本田雅阁" value="">
                        </div>
                    </div>
                     <div id="form9div">
                        <label for="form9">汽车价格*</label>
                         <div class="ui-select">
                         	<div id="form9-button" style="position:relative;">
                            	<span style="position:absolute; left:0; top:0;">&lt;10W</span>
                                <select id="car_price" name="car_price" style="position:absolute; left:0; top:0;">
                                    <option value="<10W">&lt;10W</option>
                                    <option value="10-20W" selected>10-20W</option>
                                    <option value="20-30W">20-30W</option>
                                    <option value="30-40W">30-40W</option>
                                    <option value="40-50W">40-50W</option>
                                    <option value=">50W">&gt;50W</option>
                                </select>
                             </div>
                          </div>
                    </div>
                    <div>
                        <label for="form8">所在城市*</label>
                        <div class="ui-input-text ui-body-inherit ui-corner-all ui-shadow-inset">
                        	<input type="text" id="city" name="city" placeholder="请填写例如：宁波市" value="">
                        </div>
                    </div>
                    <div>
                        <button type="submit" style="background:#3071b9;color:#fff" class="ui-btn ui-shadow ui-corner-all">申 请 加 油 补 贴</button>  
                    </div>
                </form>
            </div><!-- /content -->
    </div><!-- /page -->
     
</div>
<div class="ui-loader ui-corner-all ui-body-a ui-loader-default">
	<span class="ui-icon-loading"></span>
    <h1>loading</h1>
</div> 

</body>
</html>