package com.space.core;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.security.util.Resources_zh_TW;

import javax.annotation.Resources;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lucifel on 19-7-31.
 */
public class ClassPathXmlApplicationContext implements ApplicationContenxt {

    private Map<String,Object> iocContainer = new HashMap<String,Object>();

    public ClassPathXmlApplicationContext(String xmlPath) {
        SAXReader reader = new SAXReader();
        InputStream resourceAsStream = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream("bean.xml");
        try {
            Document document = reader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator("bean");
            while (iterator.hasNext()){
                Element bean = iterator.next();
                String id = bean.attributeValue("id");
                String className = bean.attributeValue("class");

                Class<?> beanClass = Class.forName(className);
                Constructor<?> constructor = beanClass.getConstructor();
                Object newInstance = constructor.newInstance();

                Iterator<Element> property = bean.elementIterator("property");
                while (property.hasNext()){
                    Element property_Element = property.next();
                    String fieldName = property_Element.attributeValue("name");
                    String fieldValue = property_Element.attributeValue("value");

                    String methodName = "set" + fieldName.toUpperCase().substring(0,1) + fieldName.substring(1);
                    Field field = beanClass.getDeclaredField(fieldName);
                    Method method = beanClass.getDeclaredMethod(methodName,field.getType());
                    Object value = null;
                    String fieldType = field.getType().getName();
                    if("long".equals(fieldType)){
                        value = Long.parseLong(fieldValue);
                    }
                    if("int".equals(fieldType)){
                        value = Integer.parseInt(fieldValue);
                    }
                    if("java.lang.String".equals(fieldType)){
                        value = fieldValue;
                    }
                    method.invoke(newInstance,value);

                }
                //将对象存入容器中
                iocContainer.put(id,newInstance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Object getBean(String name) {
        return iocContainer.get(name);
    }
}
