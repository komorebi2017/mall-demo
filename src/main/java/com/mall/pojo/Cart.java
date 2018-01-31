package com.mall.pojo;

import lombok.*;

import java.util.Date;


/* @Data不仅包含getter，setter方法，还有equal，hashCode，toString方法*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "updateTime")
public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;


}