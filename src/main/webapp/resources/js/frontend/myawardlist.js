$(function () {
	// 从地址栏的URL里获取userAwardId
	var userAwardId = getQueryString('userAwardId');
	// 根据userAwardId获取用户奖品映射信息
	var awardUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId='
		+ userAwardId;

	$.getJSON(awardUrl, function (data) {
		if (data.success) {
			// 1 获取奖品信息并显示
			var award = data.award;
			$('#award-img').attr('src', getContextPath()+award.awardImg);
			$('#create-time').text(
				new Date(data.userAwardMap.createTime).Format("yyyy-MM-dd"));
			$('#award-name').text(award.awardName);
			$('#award-desc').text(award.awardDesc);
			var imgListHtml = '';
			// 2 若未去实体店兑换实体奖品,生成兑换礼品的二维码供商家扫码
			if (data.usedStatus==0) {
				// 若顾客已登入,则生成购买商品的二维码供商家扫描
				imgListHtml += '<div> <img src = "/o2o/frontend/generateqrcode4award?userAwardId='
					+ userAwardId
					+ '" width="100%"/></div>';
			}
			$('#imgList').html(imgListHtml);
		}
	});
	// 点击后打开右侧栏
	$('#me').click(function () {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});