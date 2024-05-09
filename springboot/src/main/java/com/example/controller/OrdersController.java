package com.example.controller;

import com.example.common.Result;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单信息表前端操作接口
 * 接口的入口，逻辑写在controller里面
 **/
@RestController
/*用于指示该类是处理HTTP请求的控制器。与普通的@Controller注解相比，@RestController更加专注于返回数据（通常是JSON或XML格式），而不是视图（例如JSP页面）。
 *当您使用@RestController时，Spring会确保所有的响应都是返回数据，而不是重定向到某个视图。*/
@RequestMapping("/orders") /*表示该控制器将处理所有发送到 /orders路径 的请求。*/
public class OrdersController {

    @Resource
    /*是Java中的一个注解，用于实现依赖注入。它通常与Spring框架或其他依赖注入框架一起使用，用于自动注入依赖项。
       当一个类被标记为@Resource注解，这意味着它期望一个外部资源（如其他bean或服务）被注入到这个字段、构造函数或setter方法中。*/
    private OrdersService ordersService;

    /**
     * 新增
     * 定义了一个Spring Boot控制器方法，用于处理添加订单的HTTP POST请求。
     * 当有人发送一个包含订单数据的POST请求到/add时，该方法会自动解析这些数据，使用服务类处理它们，并返回一个表示操作成功的响应。
     */
    @PostMapping("/add")
    /*@PostMapping是一个Spring MVC注解，用于映射HTTP POST请求到特定的处理器方法。
    /add是该方法的URL路径。这意味着当有人向/add发送POST请求时，该方法会被调用。*/
    public Result add(@RequestBody Orders orders) {
        /*这是一个公共方法，名为add，返回类型为Result。
          它接受一个参数，类型为Orders，并通过@RequestBody注解自动绑定请求体中的JSON或表单数据到这个orders对象。
          这意味着当有人发送一个包含订单数据的POST请求到/add时，这些数据会被自动解析并传递给orders对象。*/
        ordersService.add(orders); /*调用了ordersService的add方法，并将刚刚从请求中解析的orders对象传递给它。*/
        return Result.success();
         /*返回了一个表示成功的Result对象。Result是一个通用的（自定义）响应类，用于封装操作的结果。
           这通常是为了提供一个统一的响应格式，无论是成功还是失败的操作。*/
    }

    /**
     * 删除
     * 定义了一个Spring Boot控制器方法，用于处理删除订单的HTTP DELETE请求。
     * 当有人发送一个DELETE请求到包含订单ID的URL时，该方法会自动提取ID，使用服务类删除该订单，并返回一个表示操作成功的响应。
     */
    @DeleteMapping("/delete/{id}")
    /*这是一个Spring MVC注解，用于映射HTTP DELETE请求到特定的处理器方法。
     /delete/{id}是该方法的URL路径。其中{id}是一个路径变量，它将被替换为实际的ID值。*/
    public Result deleteById(@PathVariable Integer id) { /*接受一个参数，类型为Integer，并通过@PathVariable注解自动绑定路径变量到这个id参数。*/
        ordersService.deleteById(id); /*调用了ordersService的deleteById方法，并将刚刚从URL中提取的id传递给它。*/
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        /*接受一个参数，类型为List<Integer>，并通过@RequestBody注解自动绑定请求体中的JSON或表单数据到这个ids列表。
          这意味着当发送一个包含订单ID列表的DELETE请求到/delete/batch时，这些ID会被自动解析并传递给ids列表。*/
        ordersService.deleteBatch(ids); /*调用了ordersService的deleteBatch方法，并将刚刚从请求中解析的ids列表传递给它*/
        return Result.success(); /*返回了一个表示成功的Result对象*/
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Orders orders) {
        /*当有人发送一个包含订单信息的PUT请求到/update时，这些信息会被自动解析并传递给orders对象。*/
        ordersService.updateById(orders);
        return Result.success();
    }

    /**
     * 根据ID查询
     * 定义了一个Spring Boot控制器方法，用于根据ID获取订单的HTTP GET请求。
     * 当有人发送一个GET请求到包含订单ID的URL时，该方法会自动提取ID，使用服务类获取该订单，并返回一个表示操作成功的响应，其中包含获取的订单数据。
     */
    @GetMapping("/selectById/{id}") /*/selectById/{id}是该方法的URL路径。其中{id}是一个路径变量，它将被替换为实际的ID值。*/
    public Result selectById(@PathVariable Integer id) { /*接受一个参数，类型为Integer，并通过@PathVariable注解自动绑定路径变量到这个id参数。*/
        Orders orders = ordersService.selectById(id); /*调用了ordersService的selectById方法，并将刚刚从URL中提取的id传递给它。*/
        return Result.success(orders);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Orders orders ) {
        List<Orders> list = ordersService.selectAll(orders);
        /*调用了ordersService的selectAll方法，并将传入的orders对象作为参数传递。*/
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Orders orders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        /*接受三个参数：一个Orders对象（用于查询条件），一个pageNum（用于指定页码，默认为1），和一个pageSize（用于指定每页的记录数，默认为10）。*/
        /*@RequestParam注解用于绑定请求参数到方法的参数上。如果请求中没有提供相应的参数，则使用默认值。*/
        PageInfo<Orders> page = ordersService.selectPage(orders, pageNum, pageSize); /*调用ordersService的selectPage方法，并将查询结果赋值给page变量。*/
        return Result.success(page);
    }
    /*pageNum和pageSize都是整型参数，如果请求中提供的参数不是整数或者超出了合理的范围（例如负数或非常大/小的数字），那么它们可能会被自动转换为默认值。
    为了避免这种情况，可以添加一些验证逻辑来确保参数的有效性。*/

}