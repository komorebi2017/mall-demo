package com.mall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/2/10 17:54
 * @ Description:
 */
@Slf4j
@Component /* 把ExceptionResolver作为Spring容器的一个Bean来注入到Spring容器中*/
public class ExceptionResolver implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("{}Exception",httpServletRequest.getRequestURI(),e);
        /* 当使用jackson2.x时使用MappingJackson2JsonView*/
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());
        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情请查看服务端日志的异常信息");
        modelAndView.addObject("data",e.toString());

        return modelAndView ;
    }
}
