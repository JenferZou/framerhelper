package com.example.entity;

import java.io.Serializable;

/**
 * 订单信息表
*/
/*Serializable接口是启用其序列化功能的接口。实现java.io.Serializable 接口的类是可序列化的。
没有实现此接口的类将不能使它们的任意状态被序列化或逆序列化。
序列化的过程，就是一个“freeze”的过程，它将一个对象freeze（冷冻）住，然后进行存储，
等到再次需要的时候，再将这个对象de-freeze就可以立即使用
当我们让实体类实现Serializable接口时，其实是在告诉JVM此类可被序列化，可被默认的序列化机制序列化。
1，存储对象在存储介质中，以便在下次使用的时候，可以很快捷的重建一个副本；
2，便于数据传输，尤其是在远程调用的时候。*/
public class Orders implements Serializable {
    /*
    * Java中的一个常见的声明，特别是在实现 Serializable接口 的类中。
    * private: 这是一个访问修饰符，表示这个字段是私有的，只能在这个类中被访问。
    *static: 这意味着这个字段属于类，而不是类的实例。所有的实例共享同一个serialVersionUID。
    *final: 这表示这个字段的值一旦被初始化后就不能再被修改。
    *long: 这是字段的数据类型，表示这是一个长整型值。
    *serialVersionUID: 这是字段的名称。当你实现Serializable接口时，Java会为你的类生成一个默认的serialVersionUID（如果这个字段不存在的话）。但是，为了确保序列化的兼容性，通常我们会显式地定义一个固定的serialVersionUID值。这样，每次我们修改了类的结构（例如添加或删除字段），serialVersionUID也会相应地改变，从而确保反序列化时的一致性。
            = 1L;: 这是为serialVersionUID字段赋的初始值。在这里，它的值被设置为1。
            *
    *简而言之，这个字段用于确保当一个对象被序列化并稍后反序列化时，它们属于同一个类版本。
    * 如果类的定义在序列化后发生了更改（例如添加或删除字段），反序列化过程会检查serialVersionUID以确保一致性。
    * 如果它们不匹配，反序列化会失败并抛出InvalidClassException。
    * */
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /*商品ID*/
    private Integer goodsId;
    /*用户Id*/
    private Integer userId;
    /*订单编号*/
    private String orderId;
    /*下单时间*/
    private String time;
    /*订单价格*/
    private Double price;
    /*订单状态*/
    private String status;
    /*关联产品，前台要把商品名称、商品图片以及商品提供者（用户）查询出来*/
    private String goodsName;
    private String img;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}