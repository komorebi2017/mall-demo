package com.mall.util;


import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import com.google.common.collect.Lists;
import com.mall.pojo.User;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/2/1 15:56
 * @ Description: 关于序列化对象的工具，例如把用户对象序列化成一个Json，获取的时候再反序列化成一个用户对象
 */

@Slf4j
public class JsonUtil {

    /* jackson提供的 */
    private static ObjectMapper objectMapper = new ObjectMapper();

    /* 这个类加载到JVM时，就会调用这个静态块，初始化这个objectMapper
     * 初始化很重要，如果这个jackson使用不好，会对业务造成影响 */
    static {


        /* 设置序列化 */

        /* 对象的所有字段全部列入 */
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        /* 取消默认转换timestamp形式
         * 在序列化的时候会默认把DATE转化成TIMESTAMPS形式*/
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);


        /* 忽略空Bean转json的错误 */
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        /* 所有日期格式都统一为以下的样式："yyyy-MM-dd HH:mm:ss"*/
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));


        /* 设置反序列化 */

        /* 忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况，防止错误*/
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /* 接下来方法：把对象转换成Json，把Json转换成对象*/


    /* 把对象转换成字符串 */
    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    /* 封装可以返回格式化好的json字符串*/
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    /* 第一个<T>代表声明方法只有一个类型T，也可以理解为将此方法声明为泛型方法
     * 第二个T是返回值的类型
     * 第三个<T>是来限制Class的类型*/
    public static <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str : objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("Parse String to object error",e);
            return null;
        }
    }

    /* 以下两种是能处理复杂对象，比如List，Map等*/
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            log.warn ("Parse String to object error",e);
            return null;
        }

    }

    /*可变长参数*/
    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>...elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to object error",e);
            return null;
        }

    }




    public static void main(String[] args){
        User u1 = new User();
        u1.setId(1);
        u1.setEmail("aaa@163.com");


        User u2 = new User();
        u2.setId(2);
        u2.setEmail("bbb@163.com");



        String user1Json = JsonUtil.obj2String(u1);
        String user1JsonPretty = JsonUtil.obj2StringPretty(u1);

        log.info("user1Json:{}",user1Json);
        log.info("user1JsonPretty:{}",user1JsonPretty);

        User user = JsonUtil.string2Obj(user1Json,User.class);

        List<User> userList = Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);

        String userListStr = JsonUtil.obj2StringPretty(userList);

        log.info("=================");
        log.info(userListStr);

        /* 如果Jackson在反序列化时，传List.class的话,里面默认变成LinkedHashMap*/
        List<User> userListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
        });

        List<User> userListObj2 = JsonUtil.string2Obj(userListStr,List.class,User.class);

        System.out.println("end");


    }



}
