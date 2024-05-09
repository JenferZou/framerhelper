package com.example.mapper;

import com.example.entity.Orders;

import java.util.List;

/**
 * 操作orders相关数据接口
 * 为了实现这个接口，需要提供具体的实现。
 * 比如使用MyBatis，需要为为OrdersMapper接口创建一个XML映射文件，其中包含每个方法的SQL语句和参数映射。然后，MyBatis会在运行时为你动态生成实现这个接口的代理对象。
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据ID查询
    */
    Orders selectById(Integer id);

    /**
      * 查询所有
    */
    List<Orders> selectAll(Orders orders);

}