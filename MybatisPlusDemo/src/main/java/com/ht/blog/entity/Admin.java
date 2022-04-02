package com.ht.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理员信息表
 * </p>
 *
 * @author ht
 * @since 2022-04-02
 */
@Getter
@Setter
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID号
     */
    @TableId(value = "adminId", type = IdType.AUTO)
    private Integer adminId;

    /**
     * 姓名
     */
    private String adminName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 电话号码
     */
    private String tel;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 身份证号
     */
    private String cardId;

    /**
     * 角色(0管理员，1教师，2学生)
     */
    private String role;


}
