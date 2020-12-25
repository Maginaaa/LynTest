package com.lyn.service.track;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.dto.track.ProjectSearchDTO;
import com.lyn.model.track.ProjectDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  简单随风    - 自动生成代码
 * @since 2020-11-20
 */
public interface ProjectService extends IService<ProjectDO> {

    /**
     * 获取全量列表
     */
    List<ProjectDO> getAll();

    /**
     * 获取Project列表
     * @return
     */
    IPage<ProjectDO> searchProjectList(ProjectSearchDTO projectSearchDTO);

    /**
     * 创建Project
     */
    boolean createProject(ProjectDO projectDO);

    /**
     * 更新Project
     */
    boolean editProject(ProjectDO projectDO);

    /**
     * 删除Project
     */
    boolean deleteProject(Integer id);

}
