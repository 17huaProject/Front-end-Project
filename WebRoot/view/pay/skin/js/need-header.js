// JavaScript Document
window.onload = function(){ 
	if(isWeiXin()){ 
		/*var p = document.getElementsByTagName('p'); 
		p[0].innerHTML = window.navigator.userAgent; */
	} 
} 
function isWeiXin(){ 
	var ua = window.navigator.userAgent.toLowerCase(); 
	if(ua.match(/MicroMessenger/i) == 'micromessenger'){ 
		document.getElementById("header").style.display="none";
		document.getElementById("content").style.marginTop="0";
		$('#content').removeClass("content");
		return true; 
	}else{ 
		return false; 
	} 
} 