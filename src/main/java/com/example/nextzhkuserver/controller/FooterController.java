package com.example.nextzhkuserver.controller;


import com.example.nextzhkuserver.entity.Footer;
import com.example.nextzhkuserver.service.FooterService;
import com.example.nextzhkuserver.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@RestController
public class FooterController {

    @Autowired
    private FooterService footerService;

    @GetMapping("/footer")
    public AjaxResult getFooter() {
        return AjaxResult.success("获取页脚成功", footerService.list().get(0));
    }

    @PutMapping("/footer")
    public AjaxResult updateFooter(Footer footer) {
        return footerService.updateById(footer) ? AjaxResult.success("修改成功") : AjaxResult.fail("修改失败");
    }

    /*
    * 公共方法
    * */
    @GetMapping("/public/footer")
    public AjaxResult getPublicFooter() {
        return AjaxResult.success("获取页脚成功", footerService.list().get(0));
    }
}
