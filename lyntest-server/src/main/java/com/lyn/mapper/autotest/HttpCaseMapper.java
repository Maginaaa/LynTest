package com.lyn.mapper.autotest;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn.bo.CreatorBO;
import com.lyn.model.autotest.HttpCaseDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-04
 */
@Repository
public interface HttpCaseMapper extends BaseMapper<HttpCaseDO> {

    List<CreatorBO> selectCreatorList();

}
