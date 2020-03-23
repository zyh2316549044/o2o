$(function () {
	//1 从URL里获取productId参数的值
	var productId = getQueryString('productId');
	//2 通过productId获取商品信息的URL
	var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
	//3 获取当前店铺设定的商品类别列表的URL
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
	//4 更新商品信息的URL
	var productPostUrl = '/o2o/shopadmin/modifyproduct';
	//5 商品添加和编辑使用的是同一个页面
	//  isEdit标识用来表明本次是添加还是编辑操作
	var isEdit = false;
	if (productId) {
		//若有productId则为编辑操作
		getInfo(productId);
		isEdit = true;
	} else {
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct';
	}

	//1 获取需要编辑的商品的信息,赋值给表单
	function getInfo(id) {
		$.getJSON(infoUrl, function (data) {
			if (data.success) {
				// 2 从返回的json当中获取product对象,并赋值给表单
				var product = data.product;
				$('#product-name').val(product.productName);
				$('#product-desc').val(product.productDesc);
				$('#priority').val(product.priority);
				$('#point').val(product.point);
				$('#normal-price').val(product.normalPrice);
				$('#promotion-price').val(product.promotionPrice);
				//3 获取原本的商品类别以及该店铺的所有商品类别列表
				var optionHtml = '';
				var optionArr = data.productCategoryList;
				var optionSelected = product.productCategory.productCategoryId;
				//4 生成前端的html商品类别列表,默认选择编辑前的商品类别
				optionArr.map(function (item, index) {
					var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
					optionHtml += '<option data-value="'
						+ item.productCategoryId + '"  '
						+ isSelect + '>'
						+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	//1 为商品添加操作该店铺下的所有商品类别列表
	function getCategory() {
		$.getJSON(categoryUrl, function (data) {
			if (data.success) {
				var productCategoryList = data.data;
				var optionHtml = '';
				productCategoryList.map(function (item, index) {
					optionHtml += '<option data-value="'
						+ item.productCategoryId + '">'
						+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		})
	}

	//1 针对商品详情图控件组,若该控件组的最后一个元素发生变化(上传了图片)
	$('.detail-img-div').on('change', '.detail-img:last-child', function () {
		//2 控件总数未达到6个,则生成新的一个文件上传控件
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	})

	//1 提交按钮的事件响应,分页对商品添加和编辑操作做不同的响应
	$('#submit').click(function () {
		//2 创建商品json对象,并从表单里面获得对应的属性值
		var product = {};
		product.productName = $('#product-name').val();
		product.productDesc = $('#product-desc').val();
		product.priority = $('#priority').val();
		product.point = $('#point').val();
		product.normalPrice = $('#normal-price').val();
		product.promotionPrice = $('#promotion-price').val();
		//3 获取选定的商品类别值
		product.productCategory = {
			productCategoryId: $('#category').find('option').not(function () {
				return !this.selected;
			}).data('value')
		}
		product.productId = productId;
		//3 获取缩略图文件流
		var thumbnail = $('#small-img')[0].files[0];
		//4 生成表单对象,用来接收参数并传递到后台
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		//5 遍历商品详情图控件,获取里面的文件流
		$('.detail-img').map(function (index, item) {
			//6  判断控件是否已经选择了文件
			if ($('.detail-img')[index].files.length > 0) {
				//7 将第i个文件流赋值给key为productImgi的表单键值对里
				formData.append('productImg' + index, $('.detail-img')[index].files[0]);
			}
		})
		//8 将product json对象转成字符流保存至表单对象key为productStr的键值对里
		formData.append('productStr', JSON.stringify(product));
		//9 获取表单里输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码!');
			return;
		}
		formData.append('verifyCodeActual', verifyCodeActual);
		//10 将数据提交给后台处理
		$.ajax({
			url: productPostUrl,
			type: 'POST',
			data: formData,
			contentType: false,
			processData: false,
			cache: false,
			success: function (data) {
				if (data.success) {
					$.toast('提交成功!');
					$('#captcha_img').click();
				} else {
					$.toast('提交失败!');
					$('#captcha_img').click();
				}
			}
		});
	})
});