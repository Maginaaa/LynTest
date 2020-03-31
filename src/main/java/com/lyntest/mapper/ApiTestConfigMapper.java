package com.lyntest.mapper;

import com.lyntest.bean.Category;
import com.lyntest.bean.DataBase;
import com.lyntest.bean.Variable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/23
 */
@Repository
@Mapper
public interface ApiTestConfigMapper {

    /** 获取全局变量列表 */
    List<Variable> selectGlobalVariable();

    /** 新增全局变量 */
    int createGlobalVariable(Variable variable);

    /** 修改全局变量 */
    int updateGlobalVariable(Variable variable);

    /** 删除全局变量 */
    int deleteGlobalVariable(int id);

    /** 分类配置-获取分类列表 */
    List<Category> selectAllCategory();

    /** 新增分类 */
    int createCategory(Category category);

    /** 修改分类 */
    int updateCategory(Category category);

    /** 删除分类 */
    int deleteCategory(int id);

    /** 获取数据库配置列表 */
    List<DataBase> selectDataBaseList();

    /** 新增数据库 */
    int createDataBase(DataBase dataBase);

    /** 修改数据库 */
    int updateDataBase(DataBase dataBase);

    /** 删除数据库 */
    int deleteDataBase(int id);

    /** 通过id获取数据库信息 */
    DataBase selectDataBaseById(int id);


}
