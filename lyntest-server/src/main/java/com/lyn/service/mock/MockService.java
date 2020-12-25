package com.lyn.service.mock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.dto.mock.MockSearchDTO;
import com.lyn.model.MockDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-17
 */
public interface MockService extends IService<MockDO> {

    /**
     * 获取mock接口列表
     * @return
     */
    IPage<MockDO> searchMockList(MockSearchDTO mockSearchDTO);

    /**
     * 创建mock接口
     */
    boolean createMock(MockDO mockDO);

    /**
     * 更新mock接口
     */
    boolean editMock(MockDO mockDO);

    /**
     * 删除mock接口
     */
    boolean deleteMock(Integer id);

    /**
     * 根据path查询mock接口信息
     */
    MockDO searchMockByPath(String path);

    void dynamicMock(HttpServletRequest request, HttpServletResponse response);

}
