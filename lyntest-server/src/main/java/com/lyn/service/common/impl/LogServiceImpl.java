package com.lyn.service.common.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.mapper.common.LogMapper;
import com.lyn.model.LogDO;
import com.lyn.service.common.LogService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *
 * @author 
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogDO> implements LogService {

    @Override
    public IPage<LogDO> getLogPage(Integer page, Integer count, String name, Date start, Date end) {
        Page<LogDO> pager = new Page<>(page, count);
        IPage<LogDO> iPage = this.baseMapper.findLogsByUsernameAndRange(pager, name, start, end);
        return iPage;
    }

    @Override
    public IPage<LogDO> searchLogPage(Integer page, Integer count, String name, String keyword, Date start, Date end) {
        Page<LogDO> pager = new Page<>(page, count);
        IPage<LogDO> iPage = this.baseMapper.searchLogsByUsernameAndKeywordAndRange(pager, name, "%" + keyword + "%", start, end);
        return iPage;
    }

    @Override
    public IPage<String> getUserNamePage(Integer page, Integer count) {
        Page<LogDO> pager = new Page<>(page, count);
        IPage<String> iPage = this.baseMapper.getUserNames(pager);
        return iPage;
    }

    @Override
    public boolean createLog(String message, String permission, Integer userId, String username, String method, String path, Integer status) {
        LogDO record = LogDO.builder()
                .message(message)
                .userId(userId)
                .username(username)
                .statusCode(status)
                .method(method)
                .path(path)
                .build();
        if (permission != null) {
            record.setPermission(permission);
        }
        return this.baseMapper.insert(record) > 0;
    }
}
