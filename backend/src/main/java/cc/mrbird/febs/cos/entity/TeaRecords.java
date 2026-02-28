package cc.mrbird.febs.cos.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 茶叶种植与采摘日志表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeaRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联茶叶ID
     */
    private Integer teaId;

    /**
     * 阶段：如施肥、除草、采摘
     */
    private String stage;

    /**
     * 操作详情或生长状态描述
     */
    private String content;

    /**
     * 记录发生日期
     */
    private String recordDate;

    /**
     * 操作员/记录人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private String createdAt;


}
