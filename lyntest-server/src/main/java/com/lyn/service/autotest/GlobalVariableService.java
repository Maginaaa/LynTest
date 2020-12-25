package com.lyn.service.autotest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.dto.PageBaseDTO;
import com.lyn.model.autotest.GlobalVariableDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-16
 */
public interface GlobalVariableService extends IService<GlobalVariableDO> {

    /**
     * 获取变量列表
     * @return 变量列表
     */
    IPage<GlobalVariableDO> searchVariableList(PageBaseDTO pageBaseDTO);

    /**
     * 创建变量
     */
    boolean createGlobalVariable(GlobalVariableDO globalVariableDO);

    /**
     * 更新变量信息
     */
    boolean editGlobalVariable(GlobalVariableDO globalVariableDO);

    /**
     * 删除变量
     */
    boolean deleteGlobalVariable(Integer id);

}
