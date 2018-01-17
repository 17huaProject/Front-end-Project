// JavaScript Document
$(document).ready(function(){
    $('#FormA').bind('submit', function(){
        ajaxSubmit(this);
        return false;
    });
});

function ajaxSubmit(fm){
		var mobile =  $("#mobile").val();
		var lockToken =  $("#lockToken").val();
		var name =  $("#name").val();
		var email =  $("#email").val();
		var city =  $("#city").val();
		var car_type =  $("#car_type").val();
		var car_price =  $("#car_price").val();
		var sex =  $("#sex").val();
		 $.ajax({
              type: "POST",
              url: fm.action,
              data: {mobile:mobile,name:name,email:email,city:city,car_type:car_type,car_price:car_price,sex:sex,lockToken:lockToken},
              dataType: "json",
              success: function(result) {
            	  if (result.errcode == 200) {
                      alert(result.errmsg);
                  } else {
                      alert(result.errmsg);
                  } 
              },
              error: function(data) {
                  alert("error:"+data);
               }
          });
		
	}
