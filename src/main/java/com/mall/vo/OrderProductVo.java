package com.mall.vo;


import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/27 8:28
 * @ Description:
 */
@Data
public class OrderProductVo {

    private List<OrderItemVo> orderItemVoList;

    private BigDecimal productTotalPrice;

    private String imageHost;
}
