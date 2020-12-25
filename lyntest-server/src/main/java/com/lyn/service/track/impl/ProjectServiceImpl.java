package com.lyn.service.track.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.dto.track.ProjectSearchDTO;
import com.lyn.mapper.track.ProjectMapper;
import com.lyn.model.track.ProjectDO;
import com.lyn.service.track.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-20
 */
@Service
@Transactional
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectDO> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<ProjectDO> getAll() {
        return new LambdaQueryChainWrapper<>(projectMapper)
                .orderByDesc(ProjectDO::getId)
                .list();
    }

    @Override
    public IPage<ProjectDO> searchProjectList(ProjectSearchDTO projectSearchDTO) {
        Page<ProjectDO> pager = new Page<>(projectSearchDTO.getPage(), projectSearchDTO.getCount());
        LambdaQueryWrapper<ProjectDO> wrapper = new QueryWrapper<ProjectDO>().lambda();
        wrapper.like(!projectSearchDTO.getName().isEmpty(), ProjectDO::getName, projectSearchDTO.getName())
                .orderByDesc(ProjectDO::getId);
        return projectMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean createProject(ProjectDO projectDO) {
        projectDO.setCreateTime(new Date());
        projectDO.setUpdateTime(new Date());
        return projectMapper.insert(projectDO) > 0;
    }

    @Override
    public boolean editProject(ProjectDO projectDO) {
        projectDO.setUpdateTime(new Date());
        return projectMapper.updateById(projectDO) > 0;
    }

    @Override
    public boolean deleteProject(Integer id) {
        return projectMapper.deleteById(id) > 0;
    }
}
