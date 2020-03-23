$(function(){
	getlist();
	function getlist(){
		$.ajax({
			url:"/o2o/shopadmin/getshoplist",
			type:"get",
			dataType:"json",
			success:function(data){
				if(data.success){
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}
	function handleUser(user){
		$('#user-name').text(user.name);
	}

	function handleList(shopList) {
		var html="";
		shopList.map(function(shop,index) {
			html += '<div class="row row-shop"> <div class="col-40">'
				+shop.shopName + '</div> <div class="col-40">'
				+shopStatus(shop.enableStatus) + '</div> <div class="col-20">'
				+goShop(shop.enableStatus,shop.shopId) + '</div> </div>';
		});
		$('.shop-wrap').html(html);
	}

	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if(status == -1){
			return '店铺非法';
		} else if(status == 1){
			return '审核通过';
		}
	}

	function goShop(status,shopId) {
		if (status == 1) {
			return '<a href="/o2o/shopadmin/shopmanagement?shopId='+shopId+'">进入</a>';
		}else{
			return '';
		}
	}

});