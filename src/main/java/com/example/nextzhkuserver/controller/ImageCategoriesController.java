package com.example.nextzhkuserver.controller;


import com.example.nextzhkuserver.entity.ImageCategories;
import com.example.nextzhkuserver.service.ImageCategoriesService;
import com.example.nextzhkuserver.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@RestController
public class ImageCategoriesController {
    @Autowired
    private ImageCategoriesService imageCategoriesService;
    @RequestMapping("/image/categories")
    public AjaxResult imageCategories(HttpServletRequest req, ImageCategories imageCategories) {
        String method = req.getMethod();
        switch (method) {
            case "GET":
                return AjaxResult.success("获取图片类型成功", imageCategoriesService.list());
            case "POST":
                return AjaxResult.success("该接口正在开发中。。。");
            default:
                return AjaxResult.error("不支持的请求方法");
        }
    }
}
