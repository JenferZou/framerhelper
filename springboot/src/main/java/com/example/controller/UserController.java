package com.example.controller;

import com.example.common.Result;
import com.example.entity.Echarts;
import com.example.entity.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")  // 设置该类下所有处理方法的统一基础路径为/user
public class UserController {

    @Resource  // 自动注入UserService实现类
    private UserService userService;

    @PostMapping("/add")  // 定义一个处理HTTP POST请求的映射方法，路径为"/user/add"
    public Result add(@RequestBody User user) {
        userService.add(user);
        // 调用userService中的add方法，将请求体中解析出的User对象作为参数添加到系统中
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        //通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器（controller）处理方法的形参中：
        // URL 中的 {xxx} 占位符可以通过@PathVariable(“xxx“) 绑定到操作方法的形参中。
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        //@PathVariable将这个路径变量映射到方法的 id 参数上
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(User user ) {   //传输的结构?name=xxx&age=xxx
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             //@RequestParam 用于将请求参数映射到方法的pageNum上
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectPage(user, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 统计用户类型比例
     */
    @GetMapping("/countUser")
    @ResponseBody
    public Object countUser() throws JsonProcessingException {
        ArrayList list = new ArrayList();
        Set<String> allLevel = userService.queryAllLevel();
        allLevel.forEach(v->{
            int countLevel = userService.countUserByLevel(v);
            Echarts echarts = new Echarts(countLevel, v);
            list.add(echarts);
            //[{value:countLevel1,name:'v1'},{value:countLevel2,name:'v2'}]
        });
       /* for (int i = 0; i < allLevel.size(); i++) {
            int countLevel = userService.countUserByLevel(allLevel.get(i));
            Echarts echarts = new Echarts(countLevel, allLevel.get(i));
            list.add(echarts);
        }*/
        ObjectMapper objectMapper = new ObjectMapper();
        //writeValueAsString(list)：将list对象输出成字符串
        String countU = objectMapper.writeValueAsString(list);
        return countU;
    }
}
