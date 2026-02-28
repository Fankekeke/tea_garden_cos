package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.TeaProducts;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ITeaProductsService extends IService<TeaProducts> {

    /**
     * 分页查询茶叶品种信息
     *
     * @param page        分页对象
     * @param teaProducts 茶叶品种信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<TeaProducts> page, TeaProducts teaProducts);
}
