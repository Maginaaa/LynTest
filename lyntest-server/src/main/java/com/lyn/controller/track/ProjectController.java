package com.lyn.controller.track;


import com.lyn.common.util.PageUtil;
import com.lyn.dto.track.ProjectSearchDTO;
import com.lyn.model.track.ProjectDO;
import com.lyn.service.track.ProjectService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
* @author  简单随风    - 自动生成代码
* @since 2020-11-20
*/
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/all")
    public List<ProjectDO> all() {
        return projectService.getAll();
    }

    @PostMapping("/create")
    public CreatedVO create(@RequestBody ProjectDO projectDO) {
        projectService.createProject(projectDO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody ProjectDO projectDO) {
        projectService.editProject(projectDO);
        return new UpdatedVO();
    }

    /**
     * TODO:后续需要删除项目内所有测试用例
      */
    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        projectService.deleteProject(id);
        return new DeletedVO();
    }

    @PostMapping("/list")
    public PageResponseVO<ProjectDO> list(@RequestBody ProjectSearchDTO projectSearchDTO) {
        return PageUtil.build(projectService.searchProjectList(projectSearchDTO));
    }

}
