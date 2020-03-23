$(function () {
	var loading = false;
	// 1 分页允许返回的最大条数,超过此数则禁止访问后台
	var maxItems = 999;
	// 2 一页返回的最大条数
	var pageSize = 10;
	// 3 获取店铺列表的URL
	var listUrl = '/o2o/frontend/listproductsbyshop';
	// 4 默认的页码
	var pageNum = 1;
	// 5 从地址栏URL里尝试获取shopId
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';
	// 6 获取本店铺信息以及商品类别信息列表的URL
	var searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId;
	// 7 获得并渲染出店铺类别列表以及区域列表以供搜索
	getSearchDivData();
	// 8 预先加载10条店铺信息
	addItems(pageSize, pageNum);

	// 9 给兑换礼品的a标签赋值兑换礼品的URL,2.0讲解
	$('#exchangelist').attr('href', '/o2o/frontend/awardlist?shopId=' +
		shopId);

	// 1 获得本店铺信息以及商品类别信息列表
	function getSearchDivData() {
		// 1 若传入parentId,则取出此一级类别下面所有二级类别
		var url = searchDivUrl;
		$
			.getJSON(
				url,
				function (data) {
					if (data.success) {
						// 2 获取后台传过来的店铺类别列表
						var shop = data.shop;
						$('#shop-cover-pic').attr('src', getContextPath()+shop.shopImg);
						$('#shop-update-time').html(
							new Date(shop.lastEditTime)
								.Format('yyyy-MM-dd'));
						$('#shop-name').html(shop.shopName);
						$('#shop-desc').html(shop.shopDesc);
						$('#shop-addr').html(shop.shopAddr);
						$('#shop-phone').html(shop.phone);
						// 5 获取后台传递的该店铺的商品类别列表
						var productCategoryList = data.productCategoryList;
						var html = '';
						// 6 遍历商品类别列表,生成可以点击搜索相应商品类别下的商品的a标签
						productCategoryList
							.map(function (item, index) {
								html += '<a href="#" class="button" data-product-search-id='
									+ item.productCategoryId
									+ '>'
									+ item.productCategoryName
									+ '</a>';
							});
						// 7 将标签集添加到area列表里
						$('#shopdetail-button-div').html(html);
					}
				});
	}

	// 2 获取分页展示的商品列表信息
	function addItems(pageSize, pageIndex) {
		// 1 拼接出查询的URL,赋空值默认去掉这个条件限制
		var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize
			+ '&productCategoryId=' + productCategoryId + '&productName='
			+ productName + '&shopId=' + shopId;
		// 2 设定加载符,若还在后台取数据则不能再次访问后台,避免多次重复加载
		loading = true;
		// 3 访问后台获取响应查询条件下的店铺列表
		$
			.getJSON(
				url,
				function (data) {
					if (data.success) {
						// 4 获取当前查询条件下店铺的总数
						maxItems = data.count;
						var html = '';
						// 5 遍历商品列表,拼接出卡片集合
						data.productList
							.map(function (item, index) {
								html += '<div class="card" data-product-id='
									+ item.productId
									+ '><div class="card-header">'
									+ item.productName
									+ '</div> <div class="card-content"> <div class="list-block media-list">'
									+ '<ul><li class="item-content"><div class="item-media"> <img src='
									+ getContextPath()
									+ item.imgAddr
									+ ' width="44"></div><div class="item-inner"><div class="item-subtitle"></div>'
									+ item.productDesc
									+ '</div></div></li></ul></div></div><div class="card-footer"><p class="color-gray">'
									+ new Date(
										item.lastEditTime)
										.Format('yyyy-MM-dd')
									+ '更新</p><span>点击查看</span></div></div>';
							});
						// 6 将卡片集合添加到目标HTML组件中
						$('.list-div').append(html);
						// 7 获取目前为止已显示的卡片总数,包括之前已经加载的
						var total = $('.list-div .card').length;
						// 8 若总数达到跟按照此查询条件列出来的总数一致时,停止后台则加载
						if (total >= maxItems) {
							// 8-1 隐藏提示符
							$('.infinite-scroll-preloader').hide();
							// 8-2 加载完毕,则注销无限加载事件,以防止不必要的加载
							// $.detachInfiniteScroll($('.infinite-scroll'));
							// 8-3 删除加载提示符
							// $('.infinite-scroll-preloader').remove();
						} else {
							// 8-3 显示加载提示符
							$('.infinite-scroll-preloader').show();
						}
						// 9 否则页码加1,继续load出新的店铺
						pageNum += 1;
						// 10 加载结束,可以再次加载了
						loading = false;
						// 10 刷新页面,显示新加载的店铺
						$.refreshScroller();
					}
				});
	}

	// 3 下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function () {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// 4 点击商品的卡片,进入该商品的详情页
	$('.list-div').on(
		'click',
		'.card',
		function (e) {
			var productId = e.currentTarget.dataset.productId;
			window.location.href = '/o2o/frontend/productdetail?productId='
				+ productId;
		});

	// 5 选定新的店铺类别后,重置页码,清空原先的商品,按照新的类别取查询
	$('#shopdetail-button-div').on(
		'click',
		'.button',
		function (e) {
			// 1 如果传递过来的是一个父类下的子类
			if (e) {
				productCategoryId = e.target.dataset.productSearchId;
				// 2 若之前已选定了别的category,则移除其选定效果,改成选定新的
				if (productCategoryId) {
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						shopCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
							.removeClass('button-fill');
					}
					// 3 由于查询条件改变,清空店铺列表在进行查询
					$('.list-div').empty();
					// 4 重置页码
					pageNum = 1;
					addItems(pageSize, pageNum);
				}

			}
		});

	// 6 需要查询的店铺名字发生改变后,重置页码,清空原先的店铺列表,按照新名字查询
	$('#search').on('change', function (e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// 8 点击打开右侧栏
	$('#me').click(function () {
		$.openPanel('#panel-right-demo')
	});

	// 9 初始化页面
	$.init();

});