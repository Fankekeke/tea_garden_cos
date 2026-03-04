package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Stories;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IStoriesService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/stories")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoriesController {

    private final IStoriesService storiesService;

    private final IUserInfoService userInfoService;

    /**
     * 分页查询茶农故事信息
     *
     * @param page    分页对象
     * @param stories 茶农故事信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<Stories> page, Stories stories) {
        return R.ok(storiesService.queryPage(page, stories));
    }

    /**
     * 查询所有茶农故事信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(storiesService.list());
    }

    /**
     * 查询所有审核通过茶农故事信息
     *
     * @return 列表
     */
    @GetMapping("/queryListApproved")
    public R queryListApproved() {
        List<Stories> storiesList = storiesService.list(Wrappers.<Stories>lambdaQuery().eq(Stories::getStatus, "通过"));
        List<UserInfo> userInfoList = userInfoService.list();
        Map<Integer, UserInfo> map = userInfoList.stream().collect(Collectors.toMap(UserInfo::getId, e -> e));

        for (Stories stories : storiesList) {
            UserInfo userInfo = map.get(stories.getUserId());
            stories.setCode(userInfo.getCode());
            stories.setName(userInfo.getName());
            stories.setSex(userInfo.getSex());
            stories.setImages(userInfo.getImages());
        }
        return R.ok(storiesList);
    }

    /**
     * 新增茶农故事信息
     *
     * @param stories 茶农故事信息
     * @return 结果
     */
    @PostMapping
    public R save(Stories stories) {
        stories.setCreatedAt(DateUtil.formatDateTime(new Date()));
        return R.ok(storiesService.save(stories));
    }

    /**
     * 修改茶农故事信息
     *
     * @param stories 茶农故事信息
     * @return 结果
     */
    @PutMapping
    public R edit(Stories stories) {
        return R.ok(storiesService.updateById(stories));
    }

    /**
     * 删除茶农故事信息
     *
     * @param ids ID集合
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(storiesService.removeByIds(ids));
    }
}
