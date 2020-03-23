package com.imooc.o2o.service.impl;

import java.util.List;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dto.ProdutCategoryExeution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		return productCategoryList;

	}


	@Override
	public ProdutCategoryExeution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {

		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao
						.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new RuntimeException("店铺类别失败");
				} else {

					return new ProdutCategoryExeution(
							ProductCategoryStateEnum.SUCCESS);
				}

			} catch (Exception e) {
				throw new RuntimeException("batchAddProductCategory error: "
						+ e.getMessage());
			}
		} else {
			return new ProdutCategoryExeution(
					ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional//里面有两步，第一步完成后等第二层执行完成在提交
	public ProdutCategoryExeution deleteProductCategory(long productCategoryId, long shopId) throws RuntimeException {
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(
					productCategoryId, shopId);
			if (effectedNum <= 0) {
				throw new RuntimeException("店铺类别删除失败");
			} else {
				return new ProdutCategoryExeution(
						ProductCategoryStateEnum.SUCCESS);
			}

		} catch (Exception e) {
			throw new RuntimeException("deleteProductCategory error: "
					+ e.getMessage());
		}
	}


}
