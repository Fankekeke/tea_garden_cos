package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ActivityParticipants;
import cc.mrbird.febs.cos.dao.ActivityParticipantsMapper;
import cc.mrbird.febs.cos.service.IActivityParticipantsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ActivityParticipantsServiceImpl extends ServiceImpl<ActivityParticipantsMapper, ActivityParticipants> implements IActivityParticipantsService {

    /**
     * 分页查询活动报名信息
     *
     * @param page                 分页对象
     * @param activityParticipants 活动报名信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<ActivityParticipants> page, ActivityParticipants activityParticipants) {
        return baseMapper.queryPage(page, activityParticipants);
    }
}
