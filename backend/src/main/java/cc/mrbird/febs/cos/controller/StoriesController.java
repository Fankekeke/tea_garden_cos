package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Stories;
import cc.mrbird.febs.cos.service.IStoriesService;
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
@RequestMapping("/cos/stories")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoriesController {

    private final IStoriesService storiesService;

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
        return R.ok(storiesService.list(Wrappers.<Stories>lambdaQuery().eq(Stories::getStatus, "通过")));
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
