package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Stories;
import cc.mrbird.febs.cos.dao.StoriesMapper;
import cc.mrbird.febs.cos.service.IStoriesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class StoriesServiceImpl extends ServiceImpl<StoriesMapper, Stories> implements IStoriesService {

    /**
     * 分页查询茶农故事信息
     *
     * @param page    分页对象
     * @param stories 茶农故事信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<Stories> page, Stories stories) {
        return baseMapper.queryPage(page, stories);
    }
}
