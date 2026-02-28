package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ActivityParticipants;
import cc.mrbird.febs.cos.service.IActivityParticipantsService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/activity-participants")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityParticipantsController {

    private final IActivityParticipantsService activityParticipantsService;

    /**
     * 分页查询活动报名信息
     *
     * @param page                 分页对象
     * @param activityParticipants 活动报名信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ActivityParticipants> page, ActivityParticipants activityParticipants) {
        return R.ok(activityParticipantsService.queryPage(page, activityParticipants));
    }

    /**
     * 查询所有活动报名信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(activityParticipantsService.list());
    }

    /**
     * 新增活动报名信息
     *
     * @param activityParticipants 活动报名信息
     * @return 结果
     */
    @PostMapping
    public R save(ActivityParticipants activityParticipants) {
        activityParticipants.setJoinTime(DateUtil.formatDateTime(new Date()));
        return R.ok(activityParticipantsService.save(activityParticipants));
    }

    /**
     * 修改活动报名信息
     *
     * @param activityParticipants 活动报名信息
     * @return 结果
     */
    @PutMapping
    public R edit(ActivityParticipants activityParticipants) {
        return R.ok(activityParticipantsService.updateById(activityParticipants));
    }

    /**
     * 删除活动报名信息
     *
     * @param ids ID集合
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(activityParticipantsService.removeByIds(ids));
    }
}
