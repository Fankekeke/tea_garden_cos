package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 茶园活动信息表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动主题
     */
    private String title;

    /**
     * 活动详情
     */
    private String content;

    /**
     * 活动举办时间
     */
    private String eventTime;

    /**
     * 活动结束时间
     */
    private String finishTime;

    /**
     * 发布人
     */
    private String publisher;

    /**
     * 发布时间
     */
    private String createdAt;


}
