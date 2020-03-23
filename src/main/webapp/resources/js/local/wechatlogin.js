$(function () {
	var imgListHtml1='';
	imgListHtml1 += '<div> <img src = "/o2o/local/generateqrcode4wechatloginfront?state=' + 1 + '" width="60%"/></div>';
	$('#wechatloginfront').html(imgListHtml1);

	var imgListHtml2='';
	imgListHtml2 += '<div> <img src = "/o2o/local/generateqrcode4wechatloginfront?state=' + 2 + '" width="60%"/></div>';
	$('#wechatloginshopadmin').html(imgListHtml2);

	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
	
})