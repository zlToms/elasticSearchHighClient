package com.tom.tz.controller;

import com.tom.tz.entity.CorditionES;
import com.tom.tz.entity.ESEntity;
import com.tom.tz.entity.UserVO;
import com.tom.tz.es.ESClient;
import com.tom.tz.service.ESSearchService;
import com.tom.tz.util.JavaBeanToMap;
import com.tom.tz.vo.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("es/")
public class ElasticsearchController {
    //收集es常用查询方法
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  ESSearchService esSearchService;

    /**
     * 所有条件都必须满足
     * 1、userVO.setEmail("lision");
     * 2、userVO.setRealName("老王");
     */
    @RequestMapping(value = "/search")
    public ResponseBean search() throws Exception {
        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        esEntity.setType("doc");
        UserVO userVO = new UserVO();
        userVO.setEmail("lision");
        userVO.setRealName("老王");
        Map<String,Object> mustMap = JavaBeanToMap.beanToMap(userVO);//这里有个bug id默认为零，这里应该去掉
        mustMap.remove("id");
        CorditionES corditionES = new CorditionES.Builder()
                .start(0)
                .size(10)
                .orderProperty("id")
                .orderType(2)
                .build();
        ResponseBean responseBean = esSearchService.get(esEntity, mustMap, corditionES, userVO);
        return responseBean;
    }


    @RequestMapping(value = "/search01")
    public ResponseBean search01(String emial,String realName) throws Exception {
        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        esEntity.setType("doc");
        UserVO userVO = new UserVO();
        userVO.setEmail(emial);
        userVO.setRealName(realName);
        Map<String,Object> mustMap = JavaBeanToMap.beanToMap(userVO);//这里有个bug id默认为零，这里应该去掉
        mustMap.remove("id");
        CorditionES corditionES = new CorditionES.Builder()
                .start(0)
                .size(10)
                .orderProperty("id")
                .orderType(2)
                .build();
        ResponseBean responseBean = esSearchService.get(esEntity, mustMap, corditionES, userVO);
        return responseBean;
    }



    /**
     * 满足其中一个
     * 1、userVO.setEmail("lision");
     * 2、userVO.setRealName("老王");
     */
    @RequestMapping(value = "/search02")
    public ResponseBean search02(String emial,String realName) throws Exception {
        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        esEntity.setType("doc");
        UserVO userVO = new UserVO();
        userVO.setEmail(emial);
        userVO.setRealName(realName);
        Map<String,Object> shouldMap = JavaBeanToMap.beanToMap(userVO);//这里有个bug id默认为零，这里应该去掉
        shouldMap.remove("id");
        CorditionES corditionES = new CorditionES.Builder()
                .start(0)
                .size(10)
                .orderProperty("id")
                .orderType(2)
                .build();
        ResponseBean responseBean = esSearchService.getShouldCor(esEntity, shouldMap, corditionES, userVO);
        return responseBean;
    }


    /**
     * 模糊匹配
     * realName  = 老刘   正确老王
     * 一个编辑距离
     * 限制一个匹配条件
     */
    @RequestMapping(value = "/search03")
    public ResponseBean search03(String realName) throws Exception {
        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        esEntity.setType("doc");
        UserVO userVO = new UserVO();
        userVO.setRealName(realName);
        Map<String,Object> shouldMap = JavaBeanToMap.beanToMap(userVO);//这里有个bug id默认为零，这里应该去掉
        shouldMap.remove("id");
        CorditionES corditionES = new CorditionES.Builder()
                .start(0)
                .size(10)
                .orderProperty("id")
                .orderType(2)
                .build();
        ResponseBean responseBean = esSearchService.getPuzzyCor(esEntity, shouldMap, corditionES, userVO);
        return responseBean;
    }




    /**
     * 精确匹配
     * realName  keyword
     * 必须完全匹配，不然没有结果
     *
     */
    @RequestMapping(value = "/search04")
    public ResponseBean search04(String realName) throws Exception {
        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        esEntity.setType("doc");
        UserVO userVO = new UserVO();
        userVO.setRealName(realName);
        Map<String,Object> shouldMap = JavaBeanToMap.beanToMap(userVO);//这里有个bug id默认为零，这里应该去掉
        shouldMap.remove("id");
        CorditionES corditionES = new CorditionES.Builder()
                .start(0)
                .size(10)
                .orderProperty("id")
                .orderType(2)
                .build();
        ResponseBean responseBean = esSearchService.getKeyword(esEntity, shouldMap, corditionES, userVO);
        return responseBean;
    }

}
