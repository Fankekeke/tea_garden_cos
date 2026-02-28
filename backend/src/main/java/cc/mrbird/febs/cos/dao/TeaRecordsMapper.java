package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.TeaRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface TeaRecordsMapper extends BaseMapper<TeaRecords> {

    /**
     * 分页查询种植记录信息
     *
     * @param page       分页对象
     * @param teaRecords 种植记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<TeaRecords> page, @Param("teaRecords") TeaRecords teaRecords);
}
