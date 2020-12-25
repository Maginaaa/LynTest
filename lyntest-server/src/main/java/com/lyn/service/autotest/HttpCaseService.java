package com.lyn.service.autotest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.bo.CreatorBO;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.dto.autotest.SearchDTO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.vo.AutoTestResponseVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-04
 */
public interface HttpCaseService extends IService<HttpCaseDO> {

    /**
     * 搜索httpCase列表
     * @param searchDTO
     * @return
     */
    IPage<HttpCaseDO> searchHttpCaseList(SearchDTO searchDTO);

    /**
     * 保存case
     * @param httpCaseDTO
     * @return
     */
    boolean saveHttpCase(HttpCaseDTO httpCaseDTO);

    /**
     * 编辑case
     * @param httpCaseDTO
     * @return
     */
    boolean editHttpCase(HttpCaseDTO httpCaseDTO);

    /**
     * 删除case
     * @param ids
     * @return
     */
    boolean deleteHttpCase(Integer[] ids);

    /**
     * case执行
     */
    AutoTestResponseVO casePreExecute(HttpCaseDTO httpCaseDTO);

    /**
     * case压力测试
     */
    AutoTestResponseVO caseDoExecute(HttpCaseDTO httpCaseDTO);

    List<CreatorBO> getCreatorList();

    void variableInit();

    HttpCaseDTO caseDoToDto(HttpCaseDO httpCaseDO);


}
