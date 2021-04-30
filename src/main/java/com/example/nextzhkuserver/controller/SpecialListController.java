package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.SpecialList;
import com.example.nextzhkuserver.service.SpecialListService;
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
public class SpecialListController {

    @Autowired
    private SpecialListService specialListService;

    // 获取专题
    @GetMapping("/special")
    public AjaxResult getSpecialList(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "20") int limit,
                                     SpecialList special) {
        if (null == special) {
            // 如果没有发送参数，获取所有专题
            return AjaxResult.success("获取所有专题成功", specialListService.list());
        } else {
            // 根据 category 获取专题
            LambdaQueryWrapper<SpecialList> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(SpecialList::getCategory, special.getCategory());
            Page<SpecialList> pager = new Page<>(page, limit);
            pager = specialListService.page(pager, wrapper);
            // 获取数据
            List<SpecialList> list = pager.getRecords();
            // 获取总页数
            long total = pager.getTotal();
            // 把数据保存到 map 中
            HashMap<String, Object> result = new HashMap<>();
            result.put("item", list);
            result.put("total", total);
            // 返回一个 JSON 对象
            return AjaxResult.success("获取专题成功", result);
        }
    }

    // 新建专题
    @PostMapping("/special")
    public AjaxResult createSpecial(SpecialList special) {
        if (specialListService.save(special)) {
            return AjaxResult.success("专题添加成功");
        } else {
            return AjaxResult.error("专题添加失败");
        }
    }

    // 根据 id 操作专题
    @RequestMapping("/special/{id}")
    public AjaxResult imageActionsById(HttpServletRequest req, @PathVariable("id") Integer id, SpecialList special) {

        LambdaQueryWrapper<SpecialList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SpecialList::getId, id);

        // 获取请求的方法名
        String method = req.getMethod();

        // 根据不同的方法名执行不同的操作
        switch (method) {
            case "GET":
                // 根据 id 获取专题
                if (null == specialListService.getOne(wrapper)) {
                    return AjaxResult.error("获取专题失败");
                } else {
                    return AjaxResult.success("获取专题成功", specialListService.getOne(wrapper));
                }
            case "PUT":
                // 根据 id 更新专题
                if (specialListService.update(special, wrapper)) {
                    return AjaxResult.success("专题修改成功");
                } else {
                    return AjaxResult.error("专题修改失败");
                }
            case "DELETE":
                // 根据 id 删除专题
                if (specialListService.remove(wrapper)) {
                    return AjaxResult.success("专题删除成功");
                } else {
                    return AjaxResult.error("专题删除失败");
                }
            default:
                return AjaxResult.fail("不支持的请求方法");

        }
    }

    /*
     * 公共接口
     * */

    // 获取首页展示的专题标题，时间
    @GetMapping("/public/special")
    public AjaxResult getHomeSpecial(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "4") int limit,
                                     Integer category) {
        LambdaQueryWrapper<SpecialList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SpecialList::getCategory, category)
                .eq(SpecialList::getStatus, 1)
                .select(SpecialList::getId, SpecialList::getTitle,SpecialList::getPublishTime)
                .last("order by id desc");
        Page<SpecialList> pager = new Page<>(page, limit);
        pager = specialListService.page(pager, wrapper);
        // 获取数据
        List<SpecialList> list = pager.getRecords();
        // 获取总页数
        long total = pager.getTotal();
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return AjaxResult.success("专题获取成功", result);
    }

    // 根据 id 获取专题内容
    @GetMapping("/public/special/{id}")
    public AjaxResult getSpecial(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<SpecialList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SpecialList::getId, id)
                .eq(SpecialList::getStatus, 1);
        return AjaxResult.success("获取专题内容成功", specialListService.getOne(wrapper));
    }

    // 获取最近更新的专题
    @GetMapping("/public/special/recent-update")
    public AjaxResult getRecentUpdate() {
        LambdaQueryWrapper<SpecialList> wrapper = Wrappers.lambdaQuery();
        wrapper.select(SpecialList::getId, SpecialList::getTitle)
                .last("order by id desc limit 6");
        return AjaxResult.success("获取最近更新专题成功", specialListService.list(wrapper));
    }

}
