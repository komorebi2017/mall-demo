package com.mall.dao;

import com.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param(value = "userId") Integer userId,
                                     @Param(value = "productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param(value = "userId")Integer userId,
                                 @Param(value = "productIdList") List<String> productIdList);

    int checkedOrUncheckedProduct(@Param(value = "userId")Integer userId,
                                  @Param(value = "checked") int checked,
                                  @Param(value = "productId")Integer productId);


    int selectCartProductCount(Integer userId);

    /* 从购物车获取已经勾选的产品 */
    List<Cart> selectCheckedCartByUserId(Integer userId);


}