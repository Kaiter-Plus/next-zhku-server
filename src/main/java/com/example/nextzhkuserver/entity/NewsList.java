package com.example.nextzhkuserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
public class NewsList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻封面
     */
    private String cover;

    /**
     * 新闻作者
     */
    private String author;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 新闻重要程度
     */
    private Integer importance;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 新闻内容
     */
    private String content;

    /**
     * 阅读数
     */
    private Long readings;

    /**
     * 原文链接
     */
    private String sourceLink;

    /**
     * 新闻展示状态
     */
    private Integer status;

    /**
     * 新闻类别
     */
    private Integer category;


}
