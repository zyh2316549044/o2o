package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.print.attribute.standard.NumberUp;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:店铺管理
 * @author: zhangyihan
 * @createDate: 2020-03-12 11:11
 * @version: 1.0
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private AreaService areaService;

	/***
	 * 店鋪管理
	 *
	 */
	@RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String,Object> modelmap = new HashMap<>();
		long shopId = HttpServletRequestUtil.getLong(request,"shopId");
		if (shopId <= 0){
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null){
				modelmap.put("redirect",true);
				modelmap.put("url","/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelmap.put("redirect",false);
				modelmap.put("shopId",currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop",currentShop);
			modelmap.put("redirect",false);

		}
		return modelmap;
	}


	/***
	 * 通过用户信息创建的店铺信息
	 *
	 */
	@RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){
		Map<String,Object> modelmap = new HashMap<>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("test");
		request.getSession().setAttribute("user",user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution shopList = shopService.getShopList(shopCondition, 0, 100);
			modelmap.put("shopList",shopList.getShopList());
			modelmap.put("user",user);
			modelmap.put("success",true);
		}catch (Exception e){
			modelmap.put("success",false);
			modelmap.put("errMsg",e.getMessage());
		}

		return modelmap;
	}



	/**
	 *
	 * 根据店铺id查询出店铺信息
	 * @return
	 */
	@RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1){
			try {

				Shop byShopId = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop",byShopId);
				modelMap.put("arealist",areaList);
				modelMap.put("success",true);

			}catch (Exception e){
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","empty shopId");
		}

		return modelMap;
	}


	/**
	 * 更新文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifyShop(HttpServletRequest request){
		Map<String,Object> modelmap = new HashMap<>();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并装换相应的实体类，包括信息以及图片信息(同前端约定好传入为shopstr)
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper objectMapper = new ObjectMapper();
		Shop shop = null;
		try {
			//用GitHub上封装好的json转实体类
			shop = objectMapper.readValue(shopStr,Shop.class);
		}catch (Exception e){
			modelmap.put("success",false);
			modelmap.put("errMsg",e.getMessage());
			return modelmap;
		}

		//接收图片获取前端传入的文件流
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		//2.修改商铺
		if (shop != null && shop.getShopId() != null){
			ShopExecution se = null;
			try {

				if (shopImg == null ){
					se = shopService.modifyShop(shop,null,null);
				}else {
					se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				}

				if (se.getState() == ShopStateEnum.SUCCESS.getState()){
					modelmap.put("success",true);
				}else {
					modelmap.put("success",false);
					modelmap.put("errMsg",se.getStateInfo());
				}
			} catch (Exception e) {
				modelmap.put("success",false);
				modelmap.put("errMsg",e.getMessage());
			}
			return modelmap;
		}else {
			modelmap.put("success",false);
			modelmap.put("errMsg","请输入店铺id");
			return modelmap;
		}

	}





	/**
	 *
	 * 查詢出shopCategories和area表
	 * @return
	 */
	@RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		Map<String,Object> modelMap = new HashMap<>();
		List<ShopCategory> shopCategories = new ArrayList<>();
		List<Area> areas = new ArrayList<>();
		try {
			shopCategories = shopCategoryService.queryShopCategory(new ShopCategory());
			areas = areaService.getAreaList();
			modelMap.put("shopCategories",shopCategories);
			modelMap.put("areas",areas);
			modelMap.put("success",true);
		}catch (Exception e){
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		return modelMap;
	}

	/**
	 * 上传文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/registerShop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelmap = new HashMap<>();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并装换相应的实体类，包括信息以及图片信息(同前端约定好传入为shopstr)
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper objectMapper = new ObjectMapper();
		Shop shop = null;
		try {
			//用GitHub上封装好的json转实体类
			shop = objectMapper.readValue(shopStr,Shop.class);
		}catch (Exception e){
			modelmap.put("success",false);
			modelmap.put("errMsg",e.getMessage());
			return modelmap;
		}

		//接收图片获取前端传入的文件流
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelmap.put("success",false);
			modelmap.put("errMsg","上传图片不能为空");
			return modelmap;
		}

		//2.注册商铺
		if (shop != null && shopImg != null){
			//从当前request中获取session，如果获取不到session，则会自动创建一个session，并返回新创建的session；如果获取到，则返回获取到的session;
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se = null;
			try {
				//shopImg.getInputStream()输入流，shopImg.getOriginalFilename()获取文件名
				se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				if (se.getState() == ShopStateEnum.CHECK.getState()){
					modelmap.put("success",true);
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size() ==0){
						shopList = new ArrayList<>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList",shopList);
				}else {
					modelmap.put("success",false);
					modelmap.put("errMsg",se.getStateInfo());
				}
			} catch (Exception e) {
				modelmap.put("success",false);
				modelmap.put("errMsg",e.getMessage());
			}
			return modelmap;
		}else {
			modelmap.put("success",false);
			modelmap.put("errMsg","请输入店铺信息");
			return modelmap;
		}

	}

}
