package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.TeaProducts;
import cc.mrbird.febs.cos.service.ITeaProductsService;
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
@RequestMapping("/cos/tea-products")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeaProductsController {

    private final ITeaProductsService teaProductsService;

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
    @PostMapping
    public R save(TeaProducts teaProducts) {
        teaProducts.setCreatedAt(DateUtil.formatDateTime(new Date()));
        return R.ok(teaProductsService.save(teaProducts));
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
