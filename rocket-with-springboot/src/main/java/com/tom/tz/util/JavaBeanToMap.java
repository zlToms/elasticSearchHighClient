package com.tom.tz.util;

import com.tom.tz.entity.ESEntity;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JavaBeanToMap {


    public static Map<String,Object> beanToMap(Object bean) throws Exception {
        Map<String,Object> map= new HashMap<>();
        BeanInfo b = Introspector.getBeanInfo(bean.getClass(),Object.class);
        PropertyDescriptor[] pds = b.getPropertyDescriptors();
        for(PropertyDescriptor pd: pds){
            String propertyName = pd.getName();
            Method m = pd.getReadMethod();
            Object propertyValue = m.invoke(bean);
            map.put(propertyName,propertyValue);
        }
        return map;
    }




}
