package com.example.controller;

import com.example.common.Result;
import com.example.entity.Projectapply;
import com.example.service.ProjectapplyService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 扶贫项目申请表前端操作接口
 **/

//@ResponseBody ＋ @Controller，若使用@Controller 注解，在对应的方法上，
// 视图解析器可以解析return的jsp,html页面，并且跳转到相应页面
//若返回json等内容到页面，则需要加@ResponseBody注解
@RestController
@RequestMapping("/projectapply")//用于建立请求的URL 和 处理请求方法之间的对应关系,value里面的请求路径是前端传过来的
public class ProjectapplyController {

    @Resource//依赖注入
    private ProjectapplyService projectapplyService;

    /**
     * 新增
     */
    @PostMapping("/add")//是@RequestMapping(value="这里写的是请求的路径",method = RequestMethod.POST)的缩写
    public Result add(@RequestBody Projectapply projectapply) {
        //@RequestBody接受前端传过来的信息，将这个JSON数据转换为Projectapply对象，然后将其传递给add方法的projectapply参数
        projectapplyService.add(projectapply);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        //通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器（controller）处理方法的形参中：
        // URL 中的 {xxx} 占位符可以通过@PathVariable(“xxx“) 绑定到操作方法的形参中。
        projectapplyService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        projectapplyService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Projectapply projectapply) {
        projectapplyService.updateById(projectapply);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        //@PathVariable将这个路径变量映射到方法的 id 参数上
        Projectapply projectapply = projectapplyService.selectById(id);
        return Result.success(projectapply);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Projectapply projectapply ) {
        List<Projectapply> list = projectapplyService.selectAll(projectapply);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Projectapply projectapply,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        //@RequestParam 用于将请求参数映射到方法的pageNum上
        PageInfo<Projectapply> page = projectapplyService.selectPage(projectapply, pageNum, pageSize);
        return Result.success(page);
    }

}