package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.TeaRecords;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ITeaRecordsService extends IService<TeaRecords> {

    /**
     * 分页查询种植记录信息
     *
     * @param page       分页对象
     * @param teaRecords 种植记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<TeaRecords> page, TeaRecords teaRecords);
}
