package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Trainings;
import cc.mrbird.febs.cos.dao.TrainingsMapper;
import cc.mrbird.febs.cos.service.ITrainingsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class TrainingsServiceImpl extends ServiceImpl<TrainingsMapper, Trainings> implements ITrainingsService {

    /**
     * 分页查询培训课程信息
     *
     * @param page      分页对象
     * @param trainings 培训课程信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<Trainings> page, Trainings trainings) {
        return baseMapper.queryPage(page, trainings);
    }
}
