package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Trainings;
import cc.mrbird.febs.cos.service.ITrainingsService;
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
@RequestMapping("/cos/trainings")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrainingsController {

    private final ITrainingsService trainingsService;

    /**
     * 分页查询培训课程信息
     *
     * @param page      分页对象
     * @param trainings 培训课程信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<Trainings> page, Trainings trainings) {
        return R.ok(trainingsService.queryPage(page, trainings));
    }

    /**
     * 查询所有培训课程信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(trainingsService.list());
    }

    /**
     * 新增培训课程信息
     *
     * @param trainings 培训课程信息
     * @return 结果
     */
    @PostMapping
    public R save(Trainings trainings) {
        trainings.setCreatedAt(DateUtil.formatDateTime(new Date()));
        return R.ok(trainingsService.save(trainings));
    }

    /**
     * 修改培训课程信息
     *
     * @param trainings 培训课程信息
     * @return 结果
     */
    @PutMapping
    public R edit(Trainings trainings) {
        return R.ok(trainingsService.updateById(trainings));
    }

    /**
     * 删除培训课程信息
     *
     * @param ids ID集合
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(trainingsService.removeByIds(ids));
    }
}
