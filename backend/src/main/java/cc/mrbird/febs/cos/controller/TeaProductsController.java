package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.TeaProducts;
import cc.mrbird.febs.cos.entity.TeaRecords;
import cc.mrbird.febs.cos.service.ITeaProductsService;
import cc.mrbird.febs.cos.service.ITeaRecordsService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/tea-products")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeaProductsController {

    private final ITeaProductsService teaProductsService;

    private final ITeaRecordsService teaRecordsService;

    /**
     * 分页查询茶叶品种信息
     *
     * @param page        分页对象
     * @param teaProducts 茶叶品种信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<TeaProducts> page, TeaProducts teaProducts) {
        return R.ok(teaProductsService.queryPage(page, teaProducts));
    }

    /**
     * 查询所有茶叶品种信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(teaProductsService.list());
    }

    /**
     * 新增茶叶品种信息
     *
     * @param teaProducts 茶叶品种信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public R save(TeaProducts teaProducts) throws FebsException {
        if (StrUtil.isEmpty(teaProducts.getRecord())) {
            throw new FebsException("请填写茶叶种植与采摘记录");
        }
        teaProductsService.save(teaProducts);
        List<TeaRecords> teaRecordsList = JSONUtil.toList(teaProducts.getRecord(), TeaRecords.class);
        teaRecordsList.forEach(teaRecords -> {
            teaRecords.setTeaId(teaProducts.getId());
        });
        teaRecordsService.saveBatch(teaRecordsList);
        teaProducts.setCreatedAt(DateUtil.formatDateTime(new Date()));
        return R.ok(true);
    }

    /**
     * 修改茶叶品种信息
     *
     * @param teaProducts 茶叶品种信息
     * @return 结果
     */
    @PutMapping
    public R edit(TeaProducts teaProducts) {
        return R.ok(teaProductsService.updateById(teaProducts));
    }

    /**
     * 删除茶叶品种信息
     *
     * @param ids ID集合
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(teaProductsService.removeByIds(ids));
    }
}
