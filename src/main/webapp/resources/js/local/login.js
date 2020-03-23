$(function() {
	// 1 登入验证的controller url
	var loginUrl = '/o2o/local/logincheck';
	// 2 从地址栏的URL里获取usertype
	//	usertype=1为customer,其余为shopowner
	var usertype=getQueryString('usertype');
	// 3 登入此数,累计登入三次失败后自动弹出验证码要求输入
	var loginCount = 0;

	$('#submit').click(function() {
		// 1 获取输入的账号
		var userName = $('#username').val();
		// 2 获取输入的密码
		var password = $('#psw').val();
		// 3 获取验证码信息
		var verifyCodeActual = $('#j_captcha').val();
		// 4 是否需要验证码验证,默认为false,即不需要
		var needVerify = false;
		// 5 若登入三次失败,
		if (loginCount >= 3) {
			// 6 就需验证码验证
			if (!verifyCodeActual) {
				$.toast('请输入验证码！');
				return;
			} else {
				needVerify = true;
			}
		}
		$.ajax({
			url : loginUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual,
				// 1 是否需要验证码验证
				needVerify : needVerify
			},
			success : function(data) {
				if (data.success) {
					$.toast('登录成功！');
					if (usertype==1) {
						// 2 若用户在前端展示系统页面则自动连接到前端展示系统首页
						window.location.href = '/o2o/frontend/index';
					} else{
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('登录失败！'+data.errMsg);
					loginCount++;
					if (loginCount >= 3) {
						// 3 登入失败三次,显示登入验证
						$('#verifyPart').show();
					}
				}
			}
		});
	});

	$('#register').click(function() {
		window.location.href = '/o2o/local/register?usertype=1';
	});

	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});