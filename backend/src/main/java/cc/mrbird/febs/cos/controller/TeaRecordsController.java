package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.TeaRecords;
import cc.mrbird.febs.cos.service.ITeaRecordsService;
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
@RequestMapping("/cos/tea-records")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeaRecordsController {

    private final ITeaRecordsService teaRecordsService;

    /**
     * 分页查询种植记录信息
     *
     * @param page       分页对象
     * @param teaRecords 种植记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<TeaRecords> page, TeaRecords teaRecords) {
        return R.ok(teaRecordsService.queryPage(page, teaRecords));
    }

    /**
     * 查询所有种植记录信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(teaRecordsService.list());
    }

    /**
     * 新增种植记录信息
     *
     * @param teaRecords 种植记录信息
     * @return 结果
     */
    @PostMapping
    public R save(TeaRecords teaRecords) {
        teaRecords.setCreatedAt(DateUtil.formatDateTime(new Date()));
        return R.ok(teaRecordsService.save(teaRecords));
    }

    /**
     * 修改种植记录信息
     *
     * @param teaRecords 种植记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(TeaRecords teaRecords) {
        return R.ok(teaRecordsService.updateById(teaRecords));
    }

    /**
     * 删除种植记录信息
     *
     * @param ids ID集合
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(teaRecordsService.removeByIds(ids));
    }
}
