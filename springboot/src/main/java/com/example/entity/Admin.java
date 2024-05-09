package com.example.entity;

import java.io.Serializable;

/**
 * 管理员
*/
/*Serializable接口是启用其序列化功能的接口。实现java.io.Serializable 接口的类是可序列化的。
没有实现此接口的类将不能使它们的任意状态被序列化或逆序列化。
序列化的过程，就是一个“freeze”的过程，它将一个对象freeze（冷冻）住，然后进行存储，
等到再次需要的时候，再将这个对象de-freeze就可以立即使用
当我们让实体类实现Serializable接口时，其实是在告诉JVM此类可被序列化，可被默认的序列化机制序列化。
1，存储对象在存储介质中，以便在下次使用的时候，可以很快捷的重建一个副本；
2，便于数据传输，尤其是在远程调用的时候。*/
public class Admin extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 头像 */
    private String avatar;
    /** 角色标识 */
    private String role;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}