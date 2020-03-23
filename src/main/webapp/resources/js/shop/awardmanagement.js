$(function () {
	// 1 获取该店铺下的奖品列表URL
	var listUrl = '/o2o/shopadmin/listawardsbyshop?pageIndex=1&pageSize=9999';
	// 2 设置奖品的可见状态
	var changeUrl = '/o2o/shopadmin/modifyaward';

	getList();
	function getList() {
		// 1 获取该店铺下的奖品列表URL
		$.getJSON(listUrl, function (data) {
			if (data.success) {
				var awardList = data.awardList;
				var tempHtml = '';
				// 2 遍历每条奖品信息, 拼接成一行显示,列表信息包括
				//   奖品名称,积分,上下架(含awardId),编辑按钮(含awardId),预览(含awardId)
				awardList.map(function (item, index) {
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// 3 若状态值为0,表示是已下架的奖品,操作变成上架
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 3 每件奖品的行信息
					tempHtml += '' + '<div class="row row-award">'
						+ '<div class="col-33">'
						+ item.awardName
						+ '</div>'
						+ '<div class="col-20">'
						+ item.point
						+ '</div>'
						+ '<div class="col-40">'
						+ '<a href="#" class="edit" data-id="'
						+ item.awardId
						+ '" data-status="'
						+ item.enableStatus
						+ '">编辑</a>'
						+ '<a href="#" class="delete" data-id="'
						+ item.awardId
						+ '" data-status="'
						+ contraryStatus
						+ '">'
						+ textOp
						+ '</a>'
						+ '<a href="#" class="preview" data-id="'
						+ item.awardId
						+ '" data-status="'
						+ item.enableStatus
						+ '">预览</a>'
						+ '</div>'
						+ '</div>';
				});
				$('.award-wrap').html(tempHtml);
			}
		});
	}

	// 2 将class为product-wrap里的a标签绑定上点击事件
	$('.award-wrap')
		.on(
			'click',
			'a',
			function (e) {
				var target = $(e.currentTarget);
				// 1 若有class edit,则点击就进入到奖品信息编辑页面,并带有awardId参数
				if (target.hasClass('edit')) {
					window.location.href = '/o2o/shopadmin/awardoperation?awardId='
						+ e.currentTarget.dataset.id;
					//2 若有class edit则调用后台功能上下架相关奖品,并带有productId参数
				} else if (target.hasClass('delete')) {
					changeItem(e.currentTarget.dataset.id,
						e.currentTarget.dataset.status);
				} else if (target.hasClass('preview')) {
					window.location.href = '/o2o/frontend/awarddetail?awardId='
						+ e.currentTarget.dataset.id;
				}
			});

	// 3 给新增按钮绑定点击事件
	$('#new').click(function () {
		window.location.href = '/o2o/shopadmin/awardoperation';
	});

	function changeItem(awardId, enableStatus) {
		// 1 定义award json对象 并添加awardId以及状态(上下架)
		var award = {};
		award.awardId = awardId;
		award.enableStatus = enableStatus;
		$.confirm('确定么?', function () {
			// 2 上下架相关商品
			$.ajax({
				url: changeUrl,
				type: 'POST',
				data: {
					awardStr: JSON.stringify(award),
					statusChange: true
				},
				dataType: 'json',
				success: function (data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}
});