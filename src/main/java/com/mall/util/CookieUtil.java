package com.mall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/2/6 10:22
 * @ Description:
 */
@Slf4j
public class CookieUtil {

    /* 写在一级域名下的domain */
    private final static String COOKIE_DOMAIN = ".mall.com";

    /*是服务端要注入到客户端浏览器上的*/
    private final static String COOKIE_NAME = "mall_login_token";

    /* 写入cookie */
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(COOKIE_NAME,token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");   /* 代表设置在根目录，*/

        /* COOKIE_DOMAIN和setPath之间的关系：
         * X: domain=".mall.com"
         * a站点：A.mall.com               cookie: domain=A.mall.com; path="/"
         * b站点：B.mall.com               cookie: domain=B.mall.com; path="/"
         * c站点：A.mall.com/test/cc       cookie: domain=A.mall.com; path="/test/cc"
         * d站点：A.mall.com/test/dd       cookie: domain=A.mall.com; path="/test/dd"
         * e站点: A.mall.com/test          cookie: domain=A.mall.com; path="/test"
         * 如果我们cookie中的domain是".mall.com"这样一个一级域名下的domain，abcde这五个都能拿到这个cookie
         * a和b：分别是A.mall.com和B.mall.com，都是二级域名，所以a拿不到b的cookie，b也拿不到a的cookie，
         *      因为他们是同级的并且都是二级域名
         * c和d：c和d能共享a的cookie，同样c和d因为path和domain的设置也能共享e的cookie，
         *      但是c拿不到d的，d拿不到c的，c和d也拿不到b的
         * */





        /* 防止脚本攻击带来的信息泄露风险，这个属性规定不许通过脚本访问cookie
         * 在使用setHttpOnly这个cookie之后，web站点就能排除cookie中的敏感信息被发送给黑客的计算机
         * 或者使用脚本的web站点的可能性
         * 代码这样设置之后，是无法通过脚本获取cookie信息的
         * 同时浏览器也不会把这个cookie发送给任何第三方，这样保证了信息的安全
         * 不能完全保障，但能提高一定的安全性*/
        cookie.setHttpOnly(true);

        /* 如果这个MaxAge不设置的话，cookie就不会写入硬盘，而是写在内存中，只在当前页面有效
         * 也就是说只有设置了这个才会写入硬盘，关闭浏览器，重启电脑这个cookie都存在*/
        cookie.setMaxAge(60*60*24*365);  /* 如果是-1代表永久，单位是秒*/
        log.info("write cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
        response.addCookie(cookie);


    }

    /* 读取cookie，在登录的时候写入cookie：writeLoginToken，
     * 在获取User的时候就要读取cookie拿到当时登录时候 存的sessionId */
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


     /*注销登录时，要把cookie删除*/
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        /* 从request里面读，从response里面写 */
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0); /* 设置为0，代表删除此cookie */
                    log.info("del cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    /* add一个有效期为0的cookie放到response里面，然后返回给浏览器，浏览器就会把这个cookie删除掉*/
                    response.addCookie(cookie);
                    return;
                }
            }
        }

    }




}
