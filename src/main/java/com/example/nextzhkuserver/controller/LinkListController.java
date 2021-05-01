package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.LinkList;
import com.example.nextzhkuserver.service.LinkListService;
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
public class LinkListController {

    @Autowired
    private LinkListService linkListService;

    @GetMapping("/link")
    public AjaxResult getLink(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "20") int limit,
                              LinkList link) {
        if (null == link) {
            // 没有参数直接获取所有链接
            return AjaxResult.success("获取所有链接成功", linkListService.list());
        } else {
            // 根据 category 获取链接
            LambdaQueryWrapper<LinkList> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LinkList::getCategory, link.getCategory());
            Page<LinkList> pager = new Page<>(page, limit);
            pager = linkListService.page(pager, wrapper);
            // 获取数据
            List<LinkList> list = pager.getRecords();
            // 获取总页数
            long total = pager.getTotal();
            // 把数据保存到 map 中
            HashMap<String, Object> result = new HashMap<>();
            result.put("item", list);
            result.put("total", total);
            // 返回一个 JSON 对象
            return AjaxResult.success("获取链接成功", result);
        }
    }

    // 添加新链接
    @PostMapping("/link")
    public AjaxResult createLink(LinkList link) {
        // 添加图片
        if (linkListService.save(link)) {
            return AjaxResult.success("链接添加成功");
        } else {
            return AjaxResult.error("链接添加失败");
        }
    }

    // 根据 id 获取链接
    @GetMapping("/link/{id}")
    public AjaxResult getLinkById(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<LinkList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LinkList::getId, id);
        if (null == linkListService.getOne(wrapper)) {
            return AjaxResult.error("获取链接失败");
        } else {
            return AjaxResult.success("获取成功成功", linkListService.getOne(wrapper));
        }
    }

    // 根据 id 更新链接
    @PutMapping("/link/{id}")
    public AjaxResult updateLinkById(@PathVariable("id") Integer id, LinkList link) {
        LambdaQueryWrapper<LinkList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LinkList::getId, id);
        if (linkListService.update(link, wrapper)) {
            return AjaxResult.success("链接修改成功");
        } else {
            return AjaxResult.error("链接修改失败");
        }
    }

    // 根据 id 删除链接
    @DeleteMapping("/link/{id}")
    public AjaxResult deleteLinkById(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<LinkList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LinkList::getId, id);
        if (linkListService.remove(wrapper)) {
            return AjaxResult.success("链接删除成功");
        } else {
            return AjaxResult.error("链接删除失败");
        }
    }

    // 获取数量
    @GetMapping("/link/count")
    public AjaxResult getLinkCount() {
        LambdaQueryWrapper<LinkList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LinkList::getStatus, 1);
        HashMap<String, Integer> result = new HashMap<>();
        result.put("total", linkListService.count());
        result.put("enabled", linkListService.count(wrapper));
        return AjaxResult.success("获取数量成功", result);
    }

    // 公共接口
    @GetMapping("/public/links")
    public AjaxResult getLinks(Integer category) {
        LambdaQueryWrapper<LinkList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LinkList::getCategory, category)
                .eq(LinkList::getStatus, 1)
                .select(LinkList::getId, LinkList::getTitle, LinkList::getLink);
        return AjaxResult.success("获取链接成功", linkListService.list(wrapper));
    }
}
