package com.tom.tz.controller;

import com.alibaba.fastjson.JSONObject;
import com.tom.tz.entity.ESEntity;
import com.tom.tz.entity.ProtocalEntity;
import com.tom.tz.entity.UserVO;
import com.tom.tz.enums.ESOperationType;
import com.tom.tz.mq.MQProducer;
import com.tom.tz.util.MysqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



//@RestController
@RequestMapping("/rocket")
public class RocketController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MQProducer mqProducer;

    private String topic = "TopicTest01";

    @RequestMapping(value = "/delete")
    public String sendrocket() {
        try {
            new MysqlUtils().deleteMysql(101);
            ProtocalEntity entiry = new ProtocalEntity();
            ESEntity esEntity = new ESEntity();
            esEntity.setId(101);
            entiry.setEsEntity(esEntity);
            esEntity.setEsType(ESOperationType.DELETE);
            esEntity.setIndex("posts");
            esEntity.setType("doc");
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entiry);
            logger.info("rocket的消息={}", jsonObject.toJSONString());
            mqProducer.sendMessage(jsonObject.toJSONString(),topic, null, null);
            return "发送rocket成功";
        } catch (Exception e) {
            logger.error("发送rocket异常：", e);
            return "发送rocket失败";
        }
    }

    @RequestMapping(value = "/update")
    public String sendrocket02() {
        try {
            new MysqlUtils().updateMysql();

            ESEntity esEntity = new ESEntity();
            esEntity.setIndex("posts");
            esEntity.setType("doc");
            esEntity.setId(101);
            esEntity.setEsType(ESOperationType.UPDATE);
            UserVO userVO = new UserVO();
            userVO.setId(101);
            userVO.setSex("2");
            userVO.setNote("36");
            ProtocalEntity protocalEntity = new ProtocalEntity();
            protocalEntity.setEsEntity(esEntity);
            protocalEntity.setObject(userVO);
            protocalEntity.setClazz(userVO.getClass().toString().replace("class","").trim());

            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(protocalEntity);
            logger.info("rocket的消息={}", jsonObject.toJSONString());
            mqProducer.sendMessage(jsonObject.toJSONString(),topic, null, null);
            return "发送rocket成功";
        } catch (Exception e) {
            logger.error("发送rocket异常：", e);
            return "发送rocket失败";
        }
    }

    //测试传输八大数据类型
    @RequestMapping(value = "/add")
    public String sendrocket06() throws Exception {

        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        esEntity.setType("doc");
        esEntity.setId(101);
        esEntity.setEsType(ESOperationType.ADD);
        UserVO userVO = new UserVO();
        userVO.setId(101);
        userVO.setEmail("253628343@qq.com");
        userVO.setMobile("15753247851");
        userVO.setNote("23");
        userVO.setRealName("liqinag");
        userVO.setSex("1");
        userVO.setUserName("liqiang01");
        ProtocalEntity protocalEntity = new ProtocalEntity();
        protocalEntity.setEsEntity(esEntity);
        protocalEntity.setObject(userVO);
        protocalEntity.setClazz(UserVO.class.toString().replace("class","").trim());

        new MysqlUtils().addMysql(userVO);
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(protocalEntity);
            mqProducer.sendMessage(jsonObject.toJSONString(),topic, null, null);
            return "发送rocket成功";
        } catch (Exception ex) {
            logger.error("发送rocket异常：", ex);
            return "发送rocket失败";
        }
    }

}
