package com.tom.tz.util;

import com.tom.tz.entity.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlUtils {
    //修改数据库  一条
    public void updateMysql(){
        String sql = "update t_user set sex = ?, note = ? where id =?";
        List<String> containList= new ArrayList<>();
        containList.add("sex"+":"+"2");
        containList.add("note"+":"+"2");

        List<String> conditionList= new ArrayList<>();
        conditionList.add("id"+":"+"1");

        Map<String,Object> map = new HashMap<>();
        map.put("id",101);
        map.put("sex","2");
        map.put("note","36");
        new DataUtils().updateMysql(map,sql,conditionList,containList);
    }


    //增加逻辑
    public  void addMysql(UserVO userVO) throws Exception{
        List<String> keyList= new ArrayList<>();
        keyList.add("id"+":"+"1");
        keyList.add("userName"+":"+"2");
        keyList.add("realName"+":"+"2");
        keyList.add("sex"+":"+"2");
        keyList.add("mobile"+":"+"2");
        keyList.add("email"+":"+"2");
        keyList.add("note"+":"+"2");

        Map<String,Object> map = new HashMap<>();
        map = JavaBeanToMap.beanToMap(userVO);

        String sql = "insert  into t_user (id,userName,realName,sex,mobile,email,note) " + " values (?,?,?,?,?,?,?) ";

        new DataUtils().insertData(map,sql,keyList);
    }

    //删除逻辑
    public  void deleteMysql( int id) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        new DataUtils().deleteData(map);
    }

}
