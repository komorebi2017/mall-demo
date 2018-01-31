package com.mall.pojo;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;



    /*用Set集合作用于对象时，hashCode和equals方法都是要重写的
         如果两个对象相同，既用equals比较返回True，hashCode值也一定要相同
         如果两个对象hashCode相同，它们并不一定相同
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass()!= obj.getClass()){ return false;}
        Category category = (Category) obj;
        return !(id != null ? id.equals(category.id) : category.id != null);

    }
    */
}