package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Activities;
import cc.mrbird.febs.cos.entity.ActivityParticipants;
import cc.mrbird.febs.cos.service.IActivitiesService;
import cc.mrbird.febs.cos.service.IActivityParticipantsService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/activities")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivitiesController {

    private final IActivitiesService activitiesService;

    private final IActivityParticipantsService activityParticipantsService;

    /**
     * 分页查询茶园活动信息
     *
     * @param page       分页对象
     * @param activities 茶园活动信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<Activities> page, Activities activities) {
        return R.ok(activitiesService.queryPage(page, activities));
    }

    /**
     * 查询所有茶园活动信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        List<ActivityParticipants> participantsList = activityParticipantsService.list();
        Map<Integer, List<ActivityParticipants>> map = participantsList.stream().collect(Collectors.groupingBy(ActivityParticipants::getActivityId));
        List<Activities> result = activitiesService.list();
        if (CollectionUtil.isEmpty(result)) {
            return R.ok(result);
        }
        for (Activities activities : result) {
            List<ActivityParticipants> activityParticipants = map.get(activities.getId());
            activities.setNum(CollectionUtil.isEmpty(activityParticipants) ? 0 : activityParticipants.size());
        }
        return R.ok(result);
    }

    /**
     * 新增茶园活动信息
     *
     * @param activities 茶园活动信息
     * @return 结果
     */
    @PostMapping
    public R save(Activities activities) throws FebsException {
        LocalDateTime eventTime = LocalDateTime.parse(activities.getEventTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime finishTime = LocalDateTime.parse(activities.getFinishTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (eventTime.isAfter(finishTime)) {
            throw new FebsException("活动结束时间不能早于开始时间");
        }
        activities.setCreatedAt(DateUtil.formatDateTime(new Date()));
        return R.ok(activitiesService.save(activities));
    }

    /**
     * 修改茶园活动信息
     *
     * @param activities 茶园活动信息
     * @return 结果
     */
    @PutMapping
    public R edit(Activities activities) {
        return R.ok(activitiesService.updateById(activities));
    }

    /**
     * 删除茶园活动信息
     *
     * @param ids ID集合
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(activitiesService.removeByIds(ids));
    }
}
