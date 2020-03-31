package com.lyntest.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyntest.bean.DataBase;
import com.lyntest.bean.DataBaseList;
import com.lyntest.bean.ResponseVo;
import com.lyntest.mapper.ApiTestConfigMapper;
import com.lyntest.service.DataBaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/12/16
 */
@Service(value = "DataBaseConfigService")
public class DataBaseConfigServiceImpl implements DataBaseConfigService {

    @Autowired
    private ApiTestConfigMapper apiTestConfigMapper;

    @Override
    public ResponseVo getDataBaseList(DataBaseList dataBaseList) {

        ResponseVo responseVo = new ResponseVo();

        PageHelper.startPage(dataBaseList.getPage(), dataBaseList.getPageSize());

        List<DataBase> dbList = apiTestConfigMapper.selectDataBaseList();

        PageInfo pageInfo = new PageInfo<>(dbList);

        responseVo.setIsSuccess(Boolean.TRUE);
        responseVo.setResult(pageInfo);

        return responseVo;
    }

    @Override
    public ResponseVo createNewDataBase(DataBase dataBase) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.createDataBase(dataBase);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo updateDataBase(DataBase dataBase) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.updateDataBase(dataBase);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }
        return responseVo;
    }

    @Override
    public ResponseVo deleteDataBase(int id) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.deleteDataBase(id);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }
        return responseVo;
    }
}
