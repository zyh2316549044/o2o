$(function() {
	$('#log-out').click(function(){
		//1 清除session
		$.ajax({
			url:"/o2o/local/logout",
			type:"post",
			async:false,
			cache:false,
			dateType:"json",
			success:function(data){
				if (data.success) {
					var userType = $('#log-out').attr('usertype');
					//2 清除成功后退出到登入页面
					window.location.href='/o2o/local/login?usertype='+userType;
					return false;
				}
			},
			error: function(data,error) {
				alert(error);
			}
		});
	});
});