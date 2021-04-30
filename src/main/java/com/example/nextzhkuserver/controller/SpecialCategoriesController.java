package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.LinkCategories;
import com.example.nextzhkuserver.entity.SpecialCategories;
import com.example.nextzhkuserver.service.SpecialCategoriesService;
import com.example.nextzhkuserver.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@RestController
public class SpecialCategoriesController {
    @Autowired
    private SpecialCategoriesService specialCategoriesService;

    // 获取所有专题类别
    @GetMapping("/special/categories/all")
    public AjaxResult getAllSpecialCategories() {
        return AjaxResult.success("获取所有专题类型成功", specialCategoriesService.list());
    }

    // 获取专题类别
    @GetMapping("/special/categories")
    public AjaxResult getSpecialCategories(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "20") int limit) {
        IPage<SpecialCategories> pager = new Page<>(page, limit);
        pager = specialCategoriesService.page(pager);
        // 获取数据
        List<SpecialCategories> list = pager.getRecords();
        // 获取总页数
        long total = pager.getTotal();
        // 把数据保存到 map 中
        HashMap<String, Object> result = new HashMap<>();
        result.put("item", list);
        result.put("total", total);
        return AjaxResult.success("获取专题类型成功", result);
    }

    // 创建新的专题类别
    @PostMapping("/special/categories")
    public AjaxResult createSpecialCategories(SpecialCategories specialCategories) {
        return specialCategoriesService.save(specialCategories) ?
                AjaxResult.success("创建新的专题类别成功") :
                AjaxResult.error("创建新的专题类别失败");
    }

    // 修改专题类别
    @PutMapping("/special/categories")
    public AjaxResult updateSpecialCategories(SpecialCategories specialCategories) {
        return specialCategoriesService.updateById(specialCategories) ?
                AjaxResult.success("修改专题类别成功") :
                AjaxResult.error("修改专题类别失败");
    }

    // 移除专题类别
    @DeleteMapping("/special/categories")
    public AjaxResult removeSpecialCategories(SpecialCategories specialCategories) {
        LambdaQueryWrapper<SpecialCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SpecialCategories::getId, specialCategories.getId());
        return specialCategoriesService.remove(wrapper) ?
                AjaxResult.success("移除专题类别成功") :
                AjaxResult.error("移除专题类别失败");
    }

    /*
    * 公共接口
    * */

    // 获取专题类别
    @GetMapping("/public/special/categories/home")
    public AjaxResult getHomeSpecialCategories(@RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "4") int limit) {
        LambdaQueryWrapper<SpecialCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SpecialCategories::getStatus, 1);
        IPage<SpecialCategories> pager = new Page<>(page, limit);
        pager = specialCategoriesService.page(pager, wrapper);
        List<SpecialCategories> list = pager.getRecords();
        return AjaxResult.success("获取专题类型成功", list);
    }

    // 获取所有专题类别
    @GetMapping("/public/special/categories/all")
    public AjaxResult getSpecialCategories() {
        LambdaQueryWrapper<SpecialCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SpecialCategories::getStatus, 1);
        return AjaxResult.success("获取所有专题类型成功", specialCategoriesService.list(wrapper));
    }
}
