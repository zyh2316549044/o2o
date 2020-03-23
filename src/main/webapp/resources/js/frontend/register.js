$(function() {
	var registerUrl = '/o2o/local/ownerregister';
	$('#submit').click(function() {
		var localAuth = {};
		var personInfo = {};
		// 1 用户名
		localAuth.username = $('#username').val();
		// 2 密码
		localAuth.password = $('#password').val();
		// 3 获得确认 密码
		confirm = $('#confirm_pwd').val();
		if(localAuth.password!==confirm){
			$.toast('两次输入秘密不一致!');
			return;
		}
		// 1 性别
		personInfo.gender = $('#gender').val();
		// 2 邮箱
		personInfo.email = $('#email').val();
		// 3 姓名
		personInfo.name = $('#name').val();
		// 将个人信息绑定给本地账号
		localAuth.personInfo = personInfo;
		var thumbnail = $('#small-img')[0].files[0];
		console.log(thumbnail);
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		formData.append('localAuthStr', JSON.stringify(localAuth));
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : registerUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					window.location.href = '/o2o/local/login';
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});

	$('#back').click(function() {
		window.location.href = '/o2o/local/login';
	});
});
