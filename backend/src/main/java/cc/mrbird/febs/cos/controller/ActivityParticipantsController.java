package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ActivityParticipants;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IActivityParticipantsService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

    private final IUserInfoService userInfoService;

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
    public R save(ActivityParticipants activityParticipants) throws FebsException {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, activityParticipants.getUserId()));
        if (userInfo != null) {
            activityParticipants.setUserId(userInfo.getId());
            int count = activityParticipantsService.count(Wrappers.<ActivityParticipants>lambdaQuery().eq(ActivityParticipants::getActivityId, activityParticipants.getActivityId())
                    .eq(ActivityParticipants::getUserId, userInfo.getId()));
            if (count > 0) {
                throw new FebsException("该用户已报名");
            }
        }
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
