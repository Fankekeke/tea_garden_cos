package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Trainings;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ITrainingsService extends IService<Trainings> {

    /**
     * 分页查询培训课程信息
     *
     * @param page      分页对象
     * @param trainings 培训课程信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<Trainings> page, Trainings trainings);
}
