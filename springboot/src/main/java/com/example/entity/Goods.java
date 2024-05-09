package com.example.entity;

import java.io.Serializable;

/**
 * 扶贫商品表
*/
/*Serializable接口是启用其序列化功能的接口。实现java.io.Serializable 接口的类是可序列化的。
没有实现此接口的类将不能使它们的任意状态被序列化或逆序列化。
序列化的过程，就是一个“freeze”的过程，它将一个对象freeze（冷冻）住，然后进行存储，
等到再次需要的时候，再将这个对象de-freeze就可以立即使用
当我们让实体类实现Serializable接口时，其实是在告诉JVM此类可被序列化，可被默认的序列化机制序列化。
1，存储对象在存储介质中，以便在下次使用的时候，可以很快捷的重建一个副本；
2，便于数据传输，尤其是在远程调用的时候。*/
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private String name;
    private Double price;
    private Integer num;
    private Integer sales;
    private String img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}