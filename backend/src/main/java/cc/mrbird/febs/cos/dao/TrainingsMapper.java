package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Trainings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface TrainingsMapper extends BaseMapper<Trainings> {

    /**
     * 分页查询培训课程信息
     *
     * @param page      分页对象
     * @param trainings 培训课程信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<Trainings> page, @Param("trainings") Trainings trainings);
}
