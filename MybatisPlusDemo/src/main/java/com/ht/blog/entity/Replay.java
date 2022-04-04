package com.ht.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 回复表
 * </p>
 *
 * @author ht
 * @since 2022-04-02
 */
@Getter
@Setter
public class Replay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 留言编号
     */
    private Integer messageId;

    /**
     * 回复编号
     */
    @TableId(value = "replayId", type = IdType.AUTO)
    private Integer replayId;

    /**
     * 内容
     */
    private String replay;

    /**
     * 回复时间
     */
    private LocalDate replayTime;


}
