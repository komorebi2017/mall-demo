package com.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.mall.common.ServerResponse;
import com.mall.service.IProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/18 17:21
 * @ Description:
 */

public class ProductServiceImplTest {
    @Autowired
    private IProductService productService;

    @Test
    public void getProductByKeywordCategory() throws Exception {

        ServerResponse<PageInfo> result= productService.getProductByKeywordCategory("",100002,1,10,"");
    }

}