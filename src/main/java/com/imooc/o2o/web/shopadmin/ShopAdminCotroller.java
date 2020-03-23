package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-13 11:33
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "shopadmin",method = {RequestMethod.GET})
public class ShopAdminCotroller {

    /**
     *
     * 转发到视图解析器
     *
     * @return
     */
    @RequestMapping(value = "/shopoperation")
    public String shopOpertion(){
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shoplist(){
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement")
    public String shopManagement(){
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public String productCategoryManagement(){
        return "shop/productcategorymanagement";
    }

}
