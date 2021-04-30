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
public class SpecialList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专题标题
     */
    private String title;

    /**
     * 专题作者
     */
    private String author;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 专题重要程度
     */
    private Integer importance;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 专题内容
     */
    private String content;

    /**
     * 阅读数
     */
    private Long readings;

    /**
     * 如果引用外网文章，标注原文链接
     */
    private String sourceLink;

    /**
     * 专题展示状态
     */
    private Integer status;

    /**
     * 专题类别
     */
    private Integer category;


}
