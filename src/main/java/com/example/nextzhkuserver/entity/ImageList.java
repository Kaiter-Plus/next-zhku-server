package com.example.nextzhkuserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImageList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图标base64字符串
     */
    private String content;

    /**
     * 图片加载失败时展示的信息
     */
    private String title;

    /**
     * 如果图片有超链接引用
     */
    private String href;

    /**
     * 是否展示
     */
    private Integer status;

    /**
     * 现任领导需要展示的简介
     */
    private String introduction;

    /**
     * 现任领导需要展示的成就
     */
    private String achievement;

    /**
     * 图片类别
     */
    private Integer category;


}
