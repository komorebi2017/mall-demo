package com.mall.controller.common.interceptor;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/2/10 18:48
 * @ Description:
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{

    @Override
    /* 进入controller之前，要获取用户信息，并且判断是不是管理员权限 */
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        /* 拿到请求中controller中的方法名，*/
        HandlerMethod handlerMethod = (HandlerMethod)o;

        /* 解析HandlerMethod */
        String methodName = handlerMethod.getMethod().getName();

        /* getBean拿的是controller对象*/
        String className = handlerMethod.getBean().getClass().getSimpleName();



        /* 解析参数，具体的参数key以及value是什么，以及打印日志*/
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();
            String mapValue = StringUtils.EMPTY;

             /* request这个参数的map，里面的value返回的是一个String[]数组*/
            Object obj = entry.getValue();
            if (obj instanceof String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            /* 打印日志的 */
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        User user = null;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
             user = JsonUtil.string2Obj(userJsonStr,User.class);
        }
        if (user == null ||(user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            /* 返回false，即不会调用controller里的方法*/
            /*不再用SpringMVC原生的返回，而是把Response重置托管到拦截器当中，所以要把他的属性全部重写一下*/

            /* Response重置，这里要添加reset，否则会报异常 getWriter() has already been called for this response*/
            httpServletResponse.reset();
            /* 不设置会乱码*/
            httpServletResponse.setCharacterEncoding("UTF-8");
            /* 设置返回值的类型，因为全部是Json接口 */
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();
            /* 细化逻辑*/
            if (user ==null){
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
            }else {
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
            }
            /* 把out流数据进行清空 */
            out.flush();
            out.close();
            /* 代表不需要进入controller了*/
            return false;
        }

        return true;
    }

    @Override
    /* controller处理之后 */
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    /* 所有处理完成之后 */
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
