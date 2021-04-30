package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.LinkCategories;
import com.example.nextzhkuserver.entity.NewsCategories;
import com.example.nextzhkuserver.service.LinkCategoriesService;
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
public class LinkCategoriesController {
    @Autowired
    private LinkCategoriesService linkCategoriesService;

    // 获取所有链接类别
    @GetMapping("/link/categories/all")
    public AjaxResult getAllLinkCategories() {
        return AjaxResult.success("获取所有链接类型成功", linkCategoriesService.list());
    }
    // 获取链接类别
    @GetMapping("/link/categories")
    public AjaxResult getLinkCategories(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "20") int limit) {
        IPage<LinkCategories> pager = new Page<>(page, limit);
        pager = linkCategoriesService.page(pager);
        // 获取数据
        List<LinkCategories> list = pager.getRecords();
        // 获取总页数
        long total = pager.getTotal();
        // 把数据保存到 map 中
        HashMap<String, Object> result = new HashMap<>();
        result.put("item", list);
        result.put("total", total);
        return AjaxResult.success("获取链接类型成功", result);
    }

    // 创建新的链接类别
    @PostMapping("/link/categories")
    public AjaxResult createLinkCategories(LinkCategories linkCategories) {
        return linkCategoriesService.save(linkCategories) ?
                AjaxResult.success("创建新的链接类别成功") :
                AjaxResult.error("创建新的链接类别失败");
    }

    // 修改链接类别
    @PutMapping("/link/categories")
    public AjaxResult updateLinkCategories(LinkCategories linkCategories) {
        return linkCategoriesService.updateById(linkCategories) ?
                AjaxResult.success("修改链接类别成功") :
                AjaxResult.error("修改链接类别失败");
    }

    // 移除链接类别
    @DeleteMapping("/link/categories")
    public AjaxResult removeLinkCategories(LinkCategories linkCategories) {
        LambdaQueryWrapper<LinkCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LinkCategories::getId, linkCategories.getId());
        return linkCategoriesService.remove(wrapper) ?
                AjaxResult.success("移除链接类别成功") :
                AjaxResult.error("移除链接类别失败");
    }

    // 公共接口
    @GetMapping("/public/link/categories")
    public AjaxResult getCategories() {
        LambdaQueryWrapper<LinkCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LinkCategories::getStatus, 1);
        return AjaxResult.success("获取链接类型成功", linkCategoriesService.list());
    }
}
