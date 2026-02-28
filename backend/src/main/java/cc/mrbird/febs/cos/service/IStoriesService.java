package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Stories;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IStoriesService extends IService<Stories> {

    /**
     * 分页查询茶农故事信息
     *
     * @param page    分页对象
     * @param stories 茶农故事信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<Stories> page, Stories stories);
}
