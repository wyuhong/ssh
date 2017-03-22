
//先判断lable标签中是否有内容，如果有则显示，没有就不显示
//希望传入的e是jQuery对象
function showHideError(e){
	var txt = e.text();
	//当里面没有内容，则隐藏
	if(!txt){
		e.css("display","none");
	}else{
		//当lable中有内容，则显示
		e.css("display","");
	}
}
//通过文本框推算出对应的lable设置进文本
function showHideErrorByLableId(ele,txt){
	//通过当前控件的id属性，推算出对应lable标签的id号
	var lableId =ele.attr("id")+"Error";
	//通过上一步获取得lableId，获取jQuery对象
	$("#"+lableId).text(txt);
	//根据实际情况隐藏显示lable标签
	showHideError($("#"+lableId));
	
}

//调用校验登录名函数等等
function invokeValidateFunction(iid){
	iid = iid.substring(0,1).toUpperCase() + iid.substring(1);//Loginname
	var fname = "validate" + iid;//validateLoginname
	return eval(fname + "()");//eval(string) 函数可计算某个字符串，并执行其中的的 JavaScript 代码
}
	
//这个函数专门用来校验登录名文本框的
function validateLoginname(){
	
	var bl = false;
	var value = $.trim($("#loginname").val());
	
	if(!value){
		/*//什么都没填的时候
		//通过当前控件的id属性，推算出对应lable标签的id号
		var lableId = $("#loginname").attr("id")+"Error";
		$("#"+lableId).text("用户名不能为空")
		showHideError($("#"+lableId));*/
		showHideErrorByLableId($("#loginname"),"用户名不能为空");
	}else if(value.length<6 || value.length>20){
		/*//提示用户，用户名长度必须在6-20之间
		var lableId = $("#loginname").attr("id")+"Error";
		$("#"+lableId).text("登录名长度在6-20之间")
		showHideError($("#"+lableId));*/
		$("#"+$("#loginname").attr("id")+"Error").removeClass("successClass");
		showHideErrorByLableId($("#loginname"),"登录名长度在6-20之间");
	}else{
		bl = true;
	}
	return bl;
	}
	



/*
 * 校验密码
 */
function validateLoginpass() {
	var bl = true;
	var value = $.trim($("#loginpass").val());
	if(!value) {// 非空校验
		showHideErrorByLableId($("#loginpass"),"密码不能为空");
		bl = false;
	} else if(value.length < 3 || value.length > 20) {//长度校验
		showHideErrorByLableId($("#loginpass"),"密码长度必须在3 ~ 20之间！");
		bl = false;
	}
	return bl;
}





/**
 * 校验验证码
 */

function validateVerifyCode(){
	var bl = false;
	var value = $.trim($("#verifyCode").val());
	if(!value) {// 非空校验
		showHideErrorByLableId($("#verifyCode"),"验证码不能为空！");
	}else if(value.length !=4){
		showHideErrorByLableId($("#verifyCode"),"验证码必须是四位！");
	}else {
		$.post(
				"/ssh_goods/validateVerifyCode",
				{"verifyCode":value},
				function(data){
					if(data.status=="nopass"){
						showHideErrorByLableId($("#verifyCode"),"验证码错误");
						$("#"+$("#verifyCode").attr("id")+"Error").removeClass("successClass");
					}else{
						showHideErrorByLableId($("#verifyCode"),"验证码正确");
						$("#"+$("#verifyCode").attr("id")+"Error").addClass("successClass");
						bl = true;
					}
				},
				"json"
		);

	}
	return bl;	
	
}
//设置ajax为同步提交

$.ajaxSetup({ 
	  async: false 
	  }); 


$(function(){
	//换一张 功能
	$("#repImg").click(function(){
		$("#vCode").attr("src","/ssh_goods/vcode?a="+new Date().getTime());
	});
	
    
	//当光标划过按钮的时候，替换响应图片
	$("#submit").hover(
	//当光标放入执行
			function(){
				$(this).attr("src","/ssh_goods/images/login2.jpg")
			},
	//当光标离开执行
			function(){
				$(this).attr("src","/ssh_goods/images/login1.jpg")
			}
	);
	
	
	
	//先判断lable标签中是否有内容，如果有则显示，没有就不显示
	$(".errorClass").each(function(){
		
		showHideError($(this));
	});
	

	
	//注册页面所有文本框获得焦点的操作
	$(".input").focus(function(){
		/*//通过当前控件的id属性，推算出对应lable标签的id号
		var lableId = $(this).attr("id")+"Error";
		//通过上一步获取得lableId，获取jQuery对象，并且将lable内容清空
		$("#"+lableId).text("");
		//根据实际情况隐藏显示lable标签
		showHideError($("#"+lableId));*/
		showHideErrorByLableId($(this),"");
	});
	
	//注册页面所有的文本框失去焦点的操作
	$(".input").blur(function(){
		var iid = $(this).attr("id");
		invokeValidateFunction(iid);
	});
	
	$("#loginForm").submit(function(){
		var bool = true;
		$(".input").each(function() {
			var iid = $(this).attr("id");
			//$.ajaxSettings.async(false);
			bool = invokeValidateFunction(iid);
			/*if(bool==false){
				alert("信息错误");
				return bool;
			}*/
		})
		return bool;
	});	
	
});