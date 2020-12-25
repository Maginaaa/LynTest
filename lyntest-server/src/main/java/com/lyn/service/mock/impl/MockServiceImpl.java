package com.lyn.service.mock.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn.dto.mock.MockSearchDTO;
import com.lyn.model.MockDO;
import com.lyn.mapper.MockMapper;
import com.lyn.service.mock.MockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-17
 */
@Service
public class MockServiceImpl extends ServiceImpl<MockMapper, MockDO> implements MockService {

    @Autowired
    private MockMapper mockMapper;

    @Override
    public IPage<MockDO> searchMockList(MockSearchDTO mockSearchDTO) {
        Page<MockDO> pager = new Page<>(mockSearchDTO.getPage(), mockSearchDTO.getCount());
        LambdaQueryWrapper<MockDO> wrapper = new QueryWrapper<MockDO>().lambda();
        wrapper.like(!mockSearchDTO.getPath().isEmpty(), MockDO::getPath, mockSearchDTO.getPath())
                .orderByDesc(MockDO::getId);
        return mockMapper.selectPage(pager, wrapper);
    }

    @Override
    public boolean createMock(MockDO mockDO) {
        return mockMapper.insert(mockDO) > 0;
    }

    @Override
    public boolean editMock(MockDO mockDO) {
        return mockMapper.updateById(mockDO) > 0;
    }

    @Override
    public boolean deleteMock(Integer id) {
        return mockMapper.deleteById(id) > 0;
    }

    @Override
    public MockDO searchMockByPath(String path) {

        return new LambdaQueryChainWrapper<>(mockMapper).eq(MockDO::getPath, path).one();
    }

    @Override
    public void dynamicMock(HttpServletRequest request, HttpServletResponse response) {

        String requestPath = request.getServletPath().replace("/mock/dynamic","");
        MockDO mockDO =  new LambdaQueryChainWrapper<>(mockMapper).eq(MockDO::getPath, requestPath).one();

        try {
            OutputStream outputStream = response.getOutputStream();
            byte[] dataByteArr = mockDO.getResponseBody().getBytes(StandardCharsets.UTF_8);
            outputStream.write(dataByteArr);
            response.setStatus(mockDO.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
