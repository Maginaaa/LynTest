package com.lyn.service.autotest.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.dto.PageBaseDTO;
import com.lyn.mapper.autotest.GlobalVariableMapper;
import com.lyn.model.autotest.GlobalVariableDO;
import com.lyn.service.autotest.GlobalVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-16
 */
@Service
@Transactional
public class GlobalVariableServiceImpl extends ServiceImpl<GlobalVariableMapper, GlobalVariableDO> implements GlobalVariableService {

    @Autowired
    private GlobalVariableMapper globalVariableMapper;

    @Override
    public IPage<GlobalVariableDO> searchVariableList(PageBaseDTO pageBaseDTO) {

        Page<GlobalVariableDO> pager = new Page<>(pageBaseDTO.getPage(), pageBaseDTO.getCount());
        LambdaQueryWrapper<GlobalVariableDO> wrapper = new QueryWrapper<GlobalVariableDO>().lambda();
        wrapper.orderByDesc(GlobalVariableDO::getId);
        return globalVariableMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean createGlobalVariable(GlobalVariableDO globalVariableDO) {
        return globalVariableMapper.insert(globalVariableDO) > 0;
    }

    @Override
    public boolean editGlobalVariable(GlobalVariableDO globalVariableDO) {
        return globalVariableMapper.updateById(globalVariableDO) > 0;
    }

    @Override
    public boolean deleteGlobalVariable(Integer id) {
        return globalVariableMapper.deleteById(id) > 0;
    }
}
