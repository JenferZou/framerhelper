package com.example.controller;

import com.example.common.Result;
import com.example.entity.Poorapply;
import com.example.service.PoorapplyService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 贫困户申请表前端操作接口
 **/

//@ResponseBody ＋ @Controller，若使用@Controller 注解，在对应的方法上，
// 视图解析器可以解析return的jsp,html页面，并且跳转到相应页面
//若返回json等内容到页面，则需要加@ResponseBody注解
@RestController
@RequestMapping("/poorapply")//用于建立请求的URL 和 处理请求方法之间的对应关系,value里面的请求路径是前端传过来的
public class PoorapplyController {

    @Resource//依赖注入
    private PoorapplyService poorapplyService;

    /**
     * 新增
     */
    @PostMapping("/add")//是@RequestMapping(value="这里写的是请求的路径",method = RequestMethod.POST)的缩写
    public Result add(@RequestBody Poorapply poorapply) {
        //@RequestBody接受前端传过来的信息，将这个JSON数据转换为Poorapply对象，然后将其传递给add方法的poorapply参数
        poorapplyService.add(poorapply);
        return Result.success();
    }

    /**
     * 删除
     */
    //通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器（controller）处理方法的形参中：
    // URL 中的 {xxx} 占位符可以通过@PathVariable(“xxx“) 绑定到操作方法的形参中。
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        poorapplyService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        poorapplyService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Poorapply poorapply) {
        poorapplyService.updateById(poorapply);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        //@PathVariable将这个路径变量映射到方法的 id 参数上
        Poorapply poorapply = poorapplyService.selectById(id);
        return Result.success(poorapply);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Poorapply poorapply ) {
        List<Poorapply> list = poorapplyService.selectAll(poorapply);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Poorapply poorapply,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        //@RequestParam 用于将请求参数映射到方法的pageNum上
        PageInfo<Poorapply> page = poorapplyService.selectPage(poorapply, pageNum, pageSize);
        return Result.success(page);
    }

}