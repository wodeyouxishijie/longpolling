var versionId = 0;
var max_rows = 20;
var maxMessageId = 0;
var uniqueId = 'testId';
var appId = 100001;
var typeId = 1;
$(document).ready(function(){  
	polling();
	$('.JInputButton').bind('click',function() {
	  if($.trim($('#inputTextId').val()) == "") return;
	  sendMsg();
	  $('#inputTextId').focus();
	}); 
	$(document).keydown(function(event){
	     if(event.keyCode==13){
	    	 if($('#inputTextId').val() != "") {
	    		 $('.JInputButton').click();
	    	 }
	    	 return false;
	     }
	});
});  

function sendMsg() {
	$('#JInputButton').attr("disabled","disabled");
	var inputText = $('#inputTextId').val();
	if(inputText.length > 128) {
		inputText = inputText.substring(0,128);
	}
	 $.post("/send", 
			  {"_appId":appId,
				    "_typeId":typeId,
				    "_unqId":uniqueId,
				    "_message":inputText,
				    "timestamp":(new Date()).valueOf()},
			  function(data) {
				  $('#JInputButton').attr("disabled","");  	
				  if(!data.success) {
					  	if(data.message == "NOT_LOGIN") {
					  		window.location.href="/login.html";
					  	} else {
                    		alert('发送失败！原因：'+data.message);
					  	}
                   } else {
                	   $('#inputTextId').val("");
                   }
		  	  },
		  	  "json"
	  );
}

function uploadfile(fileId) {
	$.ajaxFileUpload({  
        url:'/upload',  
        fileElementId: fileId, 
        data:{"_appId":appId,
		    "_typeId":typeId,
		    "_unqId":uniqueId},
        dataType:'json',         
        success:function(data, status){   
        },  
        error: function(data, status, e){ 
           alert('上传图片失败!');
        }  
    });  
}

function getmaxId() {
	return versionId;
}

function getMessageId() {
	return maxMessageId;
}

function polling() {
	$.ajax({      
        type:"GET",      
        dataType:"json",      
        url:"/pull",
        data:{"_appId":appId,"_typeId":typeId,"_unqId":uniqueId,"vId":getmaxId(),"msgId":getMessageId(),"timestamp":(new Date()).valueOf()},
        timeout:35000,
        success:function(data,textStatus){
            if(data.success){
            	if(data.data && data.data.length > 0) {
                	for(i=data.data.length-1;i >=0 ;i-- ) {
                		var obj = $('<li class="'+(data.data[i].self==true?'current-user':'')+
                				'"> <img width="30" height="30" class="'+(data.data[i].self==true?'avatar-r':'avatar-l')+'" src="'+data.data[i].avatar
                				+'" /> <div class="bubble"> <a class="user-name" href="#">'+data.data[i].userNick
                				+'</a> <p class="message">'+data.data[i].msg+' </p> <p class="time"> <strong>今天 </strong>'
                				+data.data[i].timeStr+' </p> </div> </li>');
	      				obj.css("opacity","0");
	      				$('#content-ul').append(obj);
	      				obj.animate({opacity:1},800);
	      				var div = $("div[class='widget-content padded']");
		  				div.scrollTop($("div[class='widget-content padded']")[0].scrollHeight);
		  				if($("#content-ul").children().length > max_rows) {
		  					$($("#content-ul").children()[0]).remove();
		  					$($("#content-ul").children()[0]).remove();
		  				}
                	}	
                	maxMessageId = data.data[data.data.length-1].messageId;
                	versionId = data.data[data.data.length-1].id;
            	}		
            } else {
            }   
        },      
        
        error:function(XMLHttpRequest,textStatus,errorThrown){      
             if(textStatus=="timeout"){      
             }
        },
        complete:function () {
        	polling();
        }
    });      
}