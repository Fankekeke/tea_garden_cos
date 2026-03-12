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
 * 活动参与人员名单
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActivityParticipants implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联活动ID
     */
    private Integer activityId;

    /**
     * 报名用户ID
     */
    private Integer userId;

    /**
     * 报名时间
     */
    private String joinTime;


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

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private String publisher;
}
