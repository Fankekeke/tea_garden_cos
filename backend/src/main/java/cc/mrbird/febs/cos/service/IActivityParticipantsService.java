package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ActivityParticipants;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IActivityParticipantsService extends IService<ActivityParticipants> {

    /**
     * 分页查询活动报名信息
     *
     * @param page                 分页对象
     * @param activityParticipants 活动报名信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<ActivityParticipants> page, ActivityParticipants activityParticipants);
}
