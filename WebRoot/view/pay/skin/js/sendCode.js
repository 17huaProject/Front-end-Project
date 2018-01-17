// JavaScript Document
var wait=60;
function time(o) {
	if (wait == 0) {
	  o.removeAttribute("disabled",false);
	  o.style.backgroundColor = '#3071b9'; 	
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
  document.getElementById("btn").onclick=function(){
	  time(this); 
}