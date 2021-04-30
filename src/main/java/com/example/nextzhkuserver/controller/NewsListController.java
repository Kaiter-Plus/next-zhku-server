package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.ImageList;
import com.example.nextzhkuserver.entity.NewsList;
import com.example.nextzhkuserver.service.NewsListService;
import com.example.nextzhkuserver.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
public class NewsListController {
    @Autowired
    private NewsListService newsListService;


    // 获取新闻
    @GetMapping("/news")
    public AjaxResult getNewsList(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "20") int limit,
                                  NewsList news) {
        if (null == news) {
            // 如果没有发送参数，获取所有新闻
            return AjaxResult.success("获取所有新闻成功", newsListService.list());
        } else {
            // 根据 category 获取新闻
            LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(NewsList::getCategory, news.getCategory());
            IPage<NewsList> pager = new Page<>(page, limit);
            pager = newsListService.page(pager, wrapper);
            // 获取数据
            List<NewsList> list = pager.getRecords();
            // 获取总页数
            long total = pager.getTotal();
            // 把数据保存到 map 中
            HashMap<String, Object> result = new HashMap<>();
            result.put("item", list);
            result.put("total", total);
            return AjaxResult.success("获取新闻成功", result);
        }
    }

    // 添加新闻
    @PostMapping("/news")
    public AjaxResult createNews(NewsList news) {
        if (newsListService.save(news)) {
            return AjaxResult.success("新闻添加成功");
        } else {
            return AjaxResult.error("新闻添加失败");
        }
    }


    // 根据 id 操作新闻
    @RequestMapping("/news/{id}")
    public AjaxResult NewsActionsById(HttpServletRequest req, @PathVariable("id") Integer id, NewsList news) {

        LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NewsList::getId, id);

        // 获取请求的方法名
        String method = req.getMethod();

        // 根据不同的方法名执行不同的操作
        switch (method) {
            case "GET":
                // 根据 id 获取新闻
                if (null == newsListService.getOne(wrapper)) {
                    return AjaxResult.error("获取新闻失败");
                } else {
                    return AjaxResult.success("获取新闻成功", newsListService.getOne(wrapper));
                }
            case "PUT":
                // 根据 id 更新新闻
                if (newsListService.update(news, wrapper)) {
                    return AjaxResult.success("新闻修改成功");
                } else {
                    return AjaxResult.error("新闻修改失败");
                }
            case "DELETE":
                // 根据 id 删除新闻
                if (newsListService.remove(wrapper)) {
                    return AjaxResult.success("新闻删除成功");
                } else {
                    return AjaxResult.error("新闻删除失败");
                }
            default:
                return AjaxResult.fail("不支持的请求方法");

        }
    }

    /*
    * 公共接口
    * */

    // 获取首页展示的新闻标题、时间
    @GetMapping("/public/news")
    public AjaxResult getHomeNews(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "20") int limit,
                                  @RequestParam(required = false, defaultValue = "false") Boolean showCover,
                                  Integer category) {
        LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NewsList::getCategory, category)
                .eq(NewsList::getStatus, 1)
                .select(NewsList::getId, NewsList::getTitle, NewsList::getPublishTime)
                .last("order by id desc");
        // 是否需要展示封面
        if (showCover) {
            wrapper.select(NewsList::getId, NewsList::getTitle, NewsList::getPublishTime, NewsList::getCover);
        }
        IPage<NewsList> pager = new Page<>(page, limit);
        pager = newsListService.page(pager, wrapper);
        // 获取数据
        List<NewsList> list = pager.getRecords();
        // 获取总页数
        long total = pager.getTotal();
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return AjaxResult.success("新闻获取成功", result);
    }

    // 获取学校介绍
    @GetMapping("/public/news/school-introduce")
    public AjaxResult getSchoolIntroduce() {
        LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NewsList::getCategory, 1).last("limit 1");;
        return AjaxResult.success("获取学校介绍成功", newsListService.getOne(wrapper));
    }

    // 获取领导关怀
    @GetMapping("/public/news/leader-care")
    public AjaxResult getLeaderCare() {
        LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NewsList::getCategory, 2)
                .last("limit 1");
        return AjaxResult.success("获取领导关怀成功", newsListService.getOne(wrapper));
    }

    // 根据 id 获取新闻内容
    @GetMapping("/public/news/{id}")
    public AjaxResult getNews(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NewsList::getId, id)
                .eq(NewsList::getStatus, 1);
        return AjaxResult.success("获取新闻成功", newsListService.getOne(wrapper));
    }

    // 获取最近更新的新闻
    @GetMapping("/public/news/recent-update")
    public AjaxResult getRecentUpdate() {
        LambdaQueryWrapper<NewsList> wrapper = Wrappers.lambdaQuery();
        wrapper.select(NewsList::getId, NewsList::getTitle)
                .last("order by id desc limit 6");
        return AjaxResult.success("获取最近更新成功", newsListService.list(wrapper));
    }
}
