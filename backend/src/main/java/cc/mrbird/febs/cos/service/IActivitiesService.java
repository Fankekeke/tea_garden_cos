package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Activities;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IActivitiesService extends IService<Activities> {

    /**
     * 分页查询茶园活动信息
     *
     * @param page       分页对象
     * @param activities 茶园活动信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<Activities> page, Activities activities);
}
