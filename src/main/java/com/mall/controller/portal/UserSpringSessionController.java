package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/10 19:09
 * @ Description:
 */
@Controller
@RequestMapping("/user/springsession/")
/* 把请求地址全部拿到/user这个命名空间下*/

public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;


    /**
     *@ Author:陌北有棵树
     *@ Date: 2018/1/10 19:11
     *@ Description: 用户登录
     *@ Param:
     *@ Return:
     */

    /* @ResponseBody:返回时自动通过SpringMVC的Jackson插件将返回值序列化成Json */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session,
                                      HttpServletResponse httpServletResponse){
        // service->mybatis->dao
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());


        }
        return response;
    }

    /**
     *@ Author:陌北有棵树
     *@ Date: 2018/1/10 20:21
     *@ Description: 用户登出
     *@ Param:
     *@ Return:
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){



        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }






    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session,HttpServletRequest httpServletRequest){


        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录需要强制登录status=10");
        }
        return iUserService.getInformation(user.getId());
    }

}
