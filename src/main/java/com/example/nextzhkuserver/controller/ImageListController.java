package com.example.nextzhkuserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nextzhkuserver.entity.ImageList;
import com.example.nextzhkuserver.service.ImageListService;
import com.example.nextzhkuserver.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@RestController
public class ImageListController {

    @Autowired
    private ImageListService imageListService;

    // 获取图片
    @GetMapping("/image")
    public AjaxResult getImageList(@RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "20") int limit,
                                   ImageList image) {
        if (null == image) {
            // 如果没有发送参数，获取所有图片
            return AjaxResult.success("获取所有图片成功", imageListService.list());
        } else {
            // 根据 category 获取图片
            LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(ImageList::getCategory, image.getCategory());
            IPage<ImageList> pager = new Page<>(page, limit);
            pager = imageListService.page(pager, wrapper);
            // 获取数据
            List<ImageList> list = pager.getRecords();
            // 获取总页数
            long total = pager.getTotal();
            // 把数据保存到 map 中
            HashMap<String, Object> result = new HashMap<>();
            result.put("item", list);
            result.put("total", total);
            // 返回一个 JSON 对象
            return AjaxResult.success("成功", result);
        }
    }

    // 添加图片
    @PostMapping("/image")
    public AjaxResult createImage(ImageList image) {
        // 添加图片
        if (imageListService.save(image)) {
            return AjaxResult.success("图片添加成功");
        } else {
            return AjaxResult.error("图片添加失败");
        }
    }

    // 根据 id 操作图片
    @RequestMapping("/image/{id}")
    public AjaxResult imageActionsById(HttpServletRequest req, @PathVariable("id") Integer id, ImageList image) {

        LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImageList::getId, id);

        // 获取请求的方法名
        String method = req.getMethod();

        // 根据不同的方法名执行不同的操作
        switch (method) {
            case "GET":
                // 根据 id 获取图片
                if (null == imageListService.getOne(wrapper)) {
                    return AjaxResult.error("获取图片失败");
                } else {
                    return AjaxResult.success("获取图片成功", imageListService.getOne(wrapper));
                }
            case "PUT":
                // 根据 id 更新图片
                if (imageListService.update(image, wrapper)) {
                    return AjaxResult.success("图片修改成功");
                } else {
                    return AjaxResult.error("图片修改失败");
                }
            case "DELETE":
                // 根据 id 删除图片
                if (imageListService.remove(wrapper)) {
                    return AjaxResult.success("图片删除成功");
                } else {
                    return AjaxResult.error("图片删除失败");
                }
            default:
                return AjaxResult.fail("不支持的请求方法");

        }
    }

    /*
     * 公共接口
     * */

    // 获取 Banner
    @GetMapping("/public/image/banner")
    public AjaxResult getBanner() {
        LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImageList::getCategory, 1)
                .last("limit 1");
        return AjaxResult.success("获取 Banner 成功", imageListService.getOne(wrapper));
    }

    // 获取轮播图
    @GetMapping("/public/image/carousel")
    public AjaxResult getCarousel() {
        LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImageList::getCategory, 2)
                .eq(ImageList::getStatus, 1);
        return AjaxResult.success("获取轮播图成功", imageListService.list(wrapper));
    }

    // 获取现任领导
    @GetMapping("/public/image/incumbent")
    public AjaxResult getIncumbent() {
        LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImageList::getCategory, 5)
                .eq(ImageList::getStatus, 1);
        return AjaxResult.success("获取现任领导成功", imageListService.list(wrapper));
    }

    // 获取校园风光
    @GetMapping("/public/image/school-scenery")
    public AjaxResult getSchoolScenery(@RequestParam(required = false, defaultValue = "1") int page,
                                       @RequestParam(required = false, defaultValue = "20") int limit) {
        // 根据 category 获取图片
        LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImageList::getCategory, 4)
                .eq(ImageList::getStatus, 1)
                .select(ImageList::getId, ImageList::getContent, ImageList::getTitle);
        IPage<ImageList> pager = new Page<>(page, limit);
        pager = imageListService.page(pager, wrapper);
        // 获取数据
        List<ImageList> list = pager.getRecords();
        // 获取总页数
        long total = pager.getTotal();
        // 把数据保存到 map 中
        HashMap<String, Object> result = new HashMap<>();
        result.put("item", list);
        result.put("total", total);
        // 返回一个 JSON 对象
        return AjaxResult.success("成功", result);
    }

    // 获取学校章程
    @GetMapping("/public/image/school-constitution")
    public AjaxResult getSchoolConstitution() {
        LambdaQueryWrapper<ImageList> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImageList::getCategory, 6)
                .last("limit 1");
        return AjaxResult.success("获取学校章程成功", imageListService.getOne(wrapper));
    }
}
