package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 茶叶品种基础信息表
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeaProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 茶叶名称
     */
    private String name;

    /**
     * 品种
     */
    private String variety;

    /**
     * 生长习性及详细描述
     */
    private String description;

    /**
     * 录入时间
     */
    private String createdAt;

    /**
     * 图片
     */
    private String images;


}
