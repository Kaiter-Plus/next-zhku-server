package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.NewsCategories;
import com.example.nextzhkuserver.entity.NewsList;
import com.example.nextzhkuserver.service.NewsCategoriesService;
import com.example.nextzhkuserver.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@RestController
public class NewsCategoriesController {
    @Autowired
    private NewsCategoriesService newsCategoriesService;

    // 获取所有链接类别
    @GetMapping("/news/categories/all")
    public AjaxResult getAllNewsCategories() {
        LambdaQueryWrapper<NewsCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(NewsCategories::getId, 1)
                .ne(NewsCategories::getId, 2);
        return AjaxResult.success("获取所有新闻类型成功", newsCategoriesService.list(wrapper));
    }

    // 获取新闻类别
    @GetMapping("/news/categories")
    public AjaxResult getNewsCategories(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "20") int limit) {
        LambdaQueryWrapper<NewsCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(NewsCategories::getId, 1)
                .ne(NewsCategories::getId, 2);
        IPage<NewsCategories> pager = new Page<>(page, limit);
        pager = newsCategoriesService.page(pager, wrapper);
        // 获取数据
        List<NewsCategories> list = pager.getRecords();
        // 获取总页数
        long total = pager.getTotal();
        // 把数据保存到 map 中
        HashMap<String, Object> result = new HashMap<>();
        result.put("item", list);
        result.put("total", total);
        return AjaxResult.success("获取新闻类型成功", result);
    }

    // 创建新的新闻类别
    @PostMapping("/news/categories")
    public AjaxResult createNewsCategories(NewsCategories newsCategories) {
        return newsCategoriesService.save(newsCategories) ?
                AjaxResult.success("创建新的新闻类别成功") :
                AjaxResult.error("创建新的新闻类别失败");
    }

    // 修改新闻类别
    @PutMapping("/news/categories")
    public AjaxResult updateNewsCategories(NewsCategories newsCategories) {
        return newsCategoriesService.updateById(newsCategories) ?
                AjaxResult.success("修改新闻类别成功") :
                AjaxResult.error("修改新闻类别失败");
    }

    // 移除新闻类别
    @DeleteMapping("/news/categories")
    public AjaxResult removeNewsCategories(NewsCategories newsCategories) {
        LambdaQueryWrapper<NewsCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NewsCategories::getId, newsCategories.getId());
        return newsCategoriesService.remove(wrapper) ?
                AjaxResult.success("移除新闻类别成功") :
                AjaxResult.error("移除新闻类别失败");
    }

    /*
     * 公共接口
     * */

    // 获取首页展示的新闻类型
    @GetMapping("/public/news/categories/home")
    public AjaxResult getHomeNewsCategories(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "4") int limit) {
        LambdaQueryWrapper<NewsCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(NewsCategories::getId, 1)
                .ne(NewsCategories::getId, 2)
                .eq(NewsCategories::getStatus, 1);
        IPage<NewsCategories> pager = new Page<>(page, limit);
        pager = newsCategoriesService.page(pager, wrapper);
        List<NewsCategories> list = pager.getRecords();
        return AjaxResult.success("获取新闻类型成功", list);
    }

    // 获取所有新闻类型
    @GetMapping("/public/news/categories/all")
    public AjaxResult getNewsCategories() {
        LambdaQueryWrapper<NewsCategories> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(NewsCategories::getId, 2)
                .ne(NewsCategories::getId, 1)
                .eq(NewsCategories::getStatus, 1);
        return AjaxResult.success("获取所有新闻类型成功", newsCategoriesService.list(wrapper));
    }
}
