package com.example.controller;

import com.example.common.Result;
import com.example.entity.Policy;
import com.example.service.PolicyService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 扶贫政策表前端操作接口
 **/
@RestController
@RequestMapping("/policy")
@Api(tags = "扶贫接口")
public class PolicyController {

    @Resource
    private PolicyService policyService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Policy policy) {
        policyService.add(policy);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        policyService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        policyService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Policy policy) {
        policyService.updateById(policy);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Policy policy = policyService.selectById(id);
        return Result.success(policy);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Policy policy ) {
        List<Policy> list = policyService.selectAll(policy);
        return Result.success(list);
    }

    @GetMapping("/top6")
    public Result top6() {
        List<Policy> list = policyService.top6();
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Policy policy,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Policy> page = policyService.selectPage(policy, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 扶贫数据分页展示接口
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "扶贫默认查询")
    @GetMapping("/selectPageList")
    public Result selectPageList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){
        return policyService.selectPageList(pageNum,pageSize);
    }

}