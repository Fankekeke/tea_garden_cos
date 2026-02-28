package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Activities;
import cc.mrbird.febs.cos.dao.ActivitiesMapper;
import cc.mrbird.febs.cos.service.IActivitiesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ActivitiesServiceImpl extends ServiceImpl<ActivitiesMapper, Activities> implements IActivitiesService {

    /**
     * 分页查询茶园活动信息
     *
     * @param page       分页对象
     * @param activities 茶园活动信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<Activities> page, Activities activities) {
        return baseMapper.queryPage(page, activities);
    }
}
