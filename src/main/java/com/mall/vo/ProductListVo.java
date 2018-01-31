package com.mall.vo;

import java.math.BigDecimal;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/13 19:48
 * @ Description:
 */


/*POJO BO VO
* POJO : java简单对象，把它定义成和数据库字段是一样的，里面不会填充业务逻辑
* 对于复杂的业务，会有BO的存在，我们会从DAO层拿到POJO，在Service做一个POJO和BO的转化，转成实际需要的业务对象
* 在Controller时把BO转成VO，转成供前端显示的对象
*
* 第二种是POJO和VO直接通信*/

public class ProductListVo {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imageHost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
