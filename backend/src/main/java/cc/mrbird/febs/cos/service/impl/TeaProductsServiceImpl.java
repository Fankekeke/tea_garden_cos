package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.TeaProducts;
import cc.mrbird.febs.cos.dao.TeaProductsMapper;
import cc.mrbird.febs.cos.service.ITeaProductsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class TeaProductsServiceImpl extends ServiceImpl<TeaProductsMapper, TeaProducts> implements ITeaProductsService {

    /**
     * 分页查询茶叶品种信息
     *
     * @param page        分页对象
     * @param teaProducts 茶叶品种信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<TeaProducts> page, TeaProducts teaProducts) {
        return baseMapper.queryPage(page, teaProducts);
    }
}
