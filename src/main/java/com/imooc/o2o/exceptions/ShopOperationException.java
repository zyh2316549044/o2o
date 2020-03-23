package com.imooc.o2o.exceptions;

/**
 * 自定义店铺操作异常
 * @author 小翁
 * @version  
 * date: 2019年7月31日 下午11:21:48 <br/> 
 * @since JDK 11
 */
public class ShopOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ShopOperationException(String msg) {
		super(msg);
	}
}
