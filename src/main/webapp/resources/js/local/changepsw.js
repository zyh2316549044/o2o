$(function () {
	// 1 修改平台密码的controller url
	var url = '/o2o/local/changelocalpwd';
	// 2 从地址栏的url里获取usertype
	//	usertype=1标识为customer,其余为shopowner
	var usertype = getQueryString('usertype');
	$('#submit').click(function () {
		// 3 获得账号
		var userName = $('#userName').val();
		// 4 获得原密码
		var password = $('#password').val();
		// 5 获得新密码
		var newPassword = $('#newPassword').val();
		// 6 获得确认密码
		var confirmPassword = $('#confirmPassword').val();
		if(userName==''||password==''||newPassword==''||confirmPassword==''){
			$.toast('请输入完整信息');
			return
		}
		if (newPassword != confirmPassword) {
			$.toast('两次输入的新密码不一致!');
			return;
		}
		if (newPassword == password) {
			$.toast('新密码和旧密码一致!');
			return;
		}
		// 7 添加表单数据
		var formData = new FormData();
		formData.append('userName', userName);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		// 8 获取验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);

		$.ajax({
			url: url,
			type: 'POST',
			data: formData,
			contentType: false,
			processData: false,
			cache: false,
			success: function (data) {
				if (data.success) {
					$.toast('提交成功！');
					if (usertype == 1) {
						window.location.href = '/o2o/frontend/index';
					} else{
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('提交失败！'+data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});

	$('#back').click(function () {
		window.location.href = '/o2o/shopadmin/shoplist';
	});

	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});
