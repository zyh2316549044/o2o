$(function() {
	// 1 从URL里获取awardId参数的值
	var awardId = getQueryString('awardId');
	// 2 通过awardId获取奖品信息的url
	var infoUrl = '/o2o/shopadmin/getawardbyid?awardId=' + awardId;
	// 3 更新奖品信息的URL
	var awardPostUrl = '/o2o/shopadmin/modifyaward';
	// 4 由于奖品添加和编辑使用的是同一个页面
	//	该标识符是用来表明本次是添加还是编辑
	var isEdit = false;
	if (awardId) {
		// 5 若有awardId则为编辑操作
		getInfo(awardId);
		isEdit = true;
	} else {
		awardPostUrl = '/o2o/shopadmin/addaward';
	}

	$("#pass-date").calendar({
	    value: ['2017-12-31']
	});

	// 1 获取需要编辑的奖品信息,并赋值给表单
	function getInfo(id) {
		$.getJSON(infoUrl, function(data) {
			if (data.success) {
				// 2 从返回的json中获取award对象的信息,赋值给表单
				var award = data.award;
				$('#award-name').val(award.awardName);
				$('#priority').val(award.priority);
				$('#award-desc').val(award.awardDesc);
				$('#point').val(award.point);
			}
		});
	}

	// 2 提交按钮事件响应,分别对奖品添加和编辑做不同响应
	$('#submit').click(function() {
		// 1 创建奖品json对象,并从表单里面获取对应的属性值
		var award = {};
		award.awardName = $('#award-name').val();
		award.priority = $('#priority').val();
		award.awardDesc = $('#award-desc').val();
		award.point = $('#point').val();
		award.awardId = awardId ? awardId : '';
		// 2 获得缩略图文件流
		var thumbnail = $('#small-img')[0].files[0];
		// 3 生成表单对象,用于接收参数并传递给后台
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		// 4 将award json对象转成字符流保存在表单对象key为awardString的键值对里
		formData.append('awardStr', JSON.stringify(award));
		// 5 获取表单里输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : awardPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					$('#captcha_img').click();
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});

});