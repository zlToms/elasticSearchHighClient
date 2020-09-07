package com.tom.tz.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "123456";

    public int getRows(String sql){
        int row = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // STEP 2: 注册mysql的驱动
            Class.forName("com.mysql.jdbc.Driver");

            // STEP 3: 获得一个连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);


           // sql = "SELECT count(*) FROM t_user ";
            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            row = rs.getInt(1);

            // STEP 6: 关闭连接
            rs.close();
            stmt.close();
            conn.close();
           
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return row;
    }

    public List<Map<String,String>>  queryData(String sql,List<String> keyList) {
        Connection conn = null;
        PreparedStatement stmt = null;
       List<Map<String,String>> list= new ArrayList();
        try {
            // STEP 2: 注册mysql的驱动
            Class.forName("com.mysql.jdbc.Driver");

            // STEP 3: 获得一个连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: 创建一个查询
            String table = "t_user";

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String,String> map = new HashMap();
                for(String key:keyList){
                    String[] split = key.split(":");
                    if(split[1].equals("1")){
                        map.put(split[0],""+rs.getInt(split[0]));
                    }else if(split[1].equals("2")){
                        map.put(split[0],rs.getString(split[0]));
                    }

                }
                list.add(map);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
            return list;
    }

    //批量插入数据
    public void bathInsert(){
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "insert  into t_user (id,userName,realName,sex,mobile,email,note) " +
                                    " values (?,?,?,?,?,?,?) ";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < 1000000; i++) {//100万条数据
                stmt.setInt(1, i+10);
                stmt.setString(2, "li"+i);
                stmt.setString(3, "老王"+i);
                stmt.setString(4, ""+1);
                stmt.setString(5, "1325314212");
                stmt.setString(6, "lision@qq");
                stmt.setString(7, "note"+i);
                //stmt.setInt(8, i);
                stmt.addBatch();
                if(i%1000==0){
                    stmt.executeBatch();
                }
            }
            stmt.executeBatch();
            conn.commit();

            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }





    //增加单个数据
    public void insertData(Map<String,Object> map,String sql,List<String> keyList){
        Connection conn = null;
        PreparedStatement stmt = null;


        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            for(int i=0; i < keyList.size();i++ ){
                String str = keyList.get(i);
                String type = str.split(":")[1];
                String name = str.split(":")[0];
                if(type.equals("1")){
                    stmt.setInt(i+1, (int)map.get(name));
                }else if(type.equals("2")){
                    stmt.setString(i+1, (String) map.get(name));
                }
            }
            stmt.addBatch();
            stmt.executeBatch();
            conn.commit();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    //修改的那个数据
    public void updateMysql(Map<String,Object> map,String sql,List<String> conditionList,List<String> containList){

        Connection conn = null;
        PreparedStatement stmt = null;

        /*String sql = "update t_user set ? = ? where ? =?";*/
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            int i =0;
            for(String str :containList ){
                i=i+1;
                String type = str.split(":")[1];
                String name = str.split(":")[0];
                if(type.equals("1")){
                    stmt.setInt(i, (int)map.get(name));
                }else if(type.equals("2")){
                    stmt.setString(i, (String) map.get(name));
                }
            }
            for(String str : conditionList){
                i=i+1;
                String type = str.split(":")[1];
                String name = str.split(":")[0];
                if(type.equals("1")){
                    stmt.setInt(i, (int)map.get(name));
                }else if(type.equals("2")){
                    stmt.setString(i, (String) map.get(name));
                }
            }
            stmt.addBatch();
            stmt.executeBatch();
            conn.commit();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }


    }


    //删除单个数据
    public void deleteData(Map<String,Object> map){

        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "delete from   t_user where id = ?" ;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (int)map.get("id"));
            stmt.addBatch();
            stmt.executeBatch();
            conn.commit();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
