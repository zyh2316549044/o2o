package com.imooc.o2o.web.superadmin;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-09 14:22
 * @version: 1.0
 */

@Controller
@RequestMapping("/superadmin")
public class AreaController {

    Logger  logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listarea",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listArea() {
        logger.info("----start-----");
         Map<String, Object> modelMap = new HashMap<String, Object>();
         List<Area> list = new ArrayList<Area>();
         try {
             list = areaService.getAreaList();
             modelMap.put("rows",list);
             modelMap.put("total",list.size());
         }catch (Exception e){
             e.printStackTrace();
             modelMap.put("success",false);
             modelMap.put("errMsg",e.toString());
         }
         logger.debug("=======");
         logger.error("test error");
         logger.info("----end---");
         return modelMap;
    }


}
