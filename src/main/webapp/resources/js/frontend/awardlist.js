$(function () {
	var loading = false;
	//1 分页允许返回的最大条数,超过此数则禁止访问后台
	var maxItems = 999;
	//2 一页返回的最大条数
	var pageSize = 10;
	//3 获取奖品列表的URL
	var listUrl = '/o2o/frontend/listawardsbyshop';
	//4 兑换奖品的url
	var exchangeUrl = '/o2o/frontend/adduserawardmap';
	//5 页码
	var pageNum = 1;
	//6 从地址栏URL里获取shopId
	var shopId = getQueryString('shopId');
	var awardName = '';
	var canProceed = false;
	var totalPoint = 0;
	//7 预先加载10条店铺信息
	addItems(pageSize, pageNum);

	//1 按照查询条件获得奖品列表,并生成对应的HTML添加到页面中
	function addItems(pageSize, pageIndex) {
		// 1 生成新条目的HTML
		var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize='
			+ pageSize + '&shopId=' + shopId + "&awardName=" + awardName;
		loading = true;
		$.getJSON(url, function (data) {
			if (data.success) {
				if(data.awardList.length==0){
					$('.infinite-scroll-preloader').remove();
					$.toast('暂无奖品')
					return;
				}
				// 2 获取总数
				maxItems = data.count;
				var html = '';
				data.awardList.map(function (item, index) {
					html += '<div class="card" data-award-id='
						+ item.awardId + ' data-point="' + item.point
						+ '"><div class="card-header">'
						+ item.awardName + '<span class="pull-right">需要积分'
						+ item.point + '</span></div>'
						+ ' <div class="card-content">'
						+ '<div class="list-block media-list">' + '<ul>'
						+ '<li class="item-content">'
						+ '<div class="item-media"> <img src='
						+ getContextPath() + item.awardImg
						+ ' width="44"></div>'
						+ '<div class="item-inner">'
						+ '<div class="item-subtitle">' + item.awardDesc
						+ '</div></div></li></ul>'
						+ '</div></div><div class="card-footer">'
						+ '<p class="color-gray">'
						+ new Date(item.lastEditTime).Format('yyyy-MM-dd') + '更新</p>';
					// 3 若用户有积分,则显示领取按钮
					if (data.totalPoint != undefined) {
						html += '<span> 点击领取</span></div></div>'
					} else {
						html += '</div></div>'
					}
				});
				$('.list-div').append(html);
				if (data.totalPoint != undefined) {
					// 4 若用户在该店铺有积分,则显示
					canProceed = true;
					$('#title').text('当前积分' + data.totalPoint);
					totalPoint = data.totalPoint;
				}
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// 5 加载完毕,则注销无限加载事件,以防不必要的加载
					$.detachInfiniteScroll($('.infinite-scroll'));
					// 6 删除加载提示符
					$('.infinite-scroll-preloader').remove();
					return;
				}
				pageNum += 1;
				loading = false;
				$.refreshScroller;
			}
		});
	}

	//2 下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function () {
		if (loading) return;
		addItems(pageSize, pageNum);
	});

	//3 点击按钮,兑换礼品
	$('.award-list').on('click', '.card', function (e) {
		// 1 若用户在该店铺有积分且积分数大于该奖品需要消耗的积分
		if (canProceed && (totalPoint > e.currentTarget.dataset.point)) {
			// 2 则弹出操作确认框
			$.confirm('需要消耗' + e.currentTarget.dataset.point + '积分,确定操作么', function () {
				// 3 访问后台,领取奖品
				$.ajax({
					url: exchangeUrl,
					type: "POST",
					data: {
						awardId: e.currentTarget.dataset.awardId
					},
					dataType: 'json',
					success: function (data) {
						if (data.success) {
							$.toast('操作成功！');
							totalPoint=totalPoint-e.currentTarget.dataset.point;
							$('#title').text('当前积分'+totalPoint);
						} else {
							$.toast('提交失败！');
						}
					}
				});
			})
		} else{
			$.toast('积分不足或者无权限操作!');
		}
	});


	//4  需要查询的店铺名字发生改变后,重置页码,清空原先的店铺列表,按照新名字查询
	$('#search').on('change', function (e) {
		awardName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});


	//5 点击打开右侧栏
	$('#me').click(function () {
		$.openPanel('#panel-right-demo')
	});

	//6 初始化页面
	$.init();

});