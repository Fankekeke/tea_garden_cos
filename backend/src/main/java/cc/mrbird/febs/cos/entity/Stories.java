package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 茶农故事分享与审核表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Stories implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者ID
     */
    private Integer userId;

    /**
     * 故事标题
     */
    private String title;

    /**
     * 故事正文
     */
    private String content;

    /**
     * 审核状态：pending-待审核, approved-通过, rejected-驳回
     */
    private String status;

    /**
     * 审核意见
     */
    private String auditRemark;

    /**
     * 发布时间
     */
    private String createdAt;



    /**
     * 用户编号
     */
    @TableField(exist = false)
    private String code;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 性别（0.男1.女）
     */
    @TableField(exist = false)
    private Integer sex;

    /**
     * 头像
     */
    @TableField(exist = false)
    private String images;

}
