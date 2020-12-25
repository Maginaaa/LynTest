package com.lyn.mapper.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn.model.GroupDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author 
 */
@Repository
public interface GroupMapper extends BaseMapper<GroupDO> {

    /**
     * 获得用户的所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<GroupDO> selectUserGroups(@Param("userId") Integer userId);

    /**
     * 获得用户的所有分组id
     *
     * @param userId 用户id
     * @return 所有分组id
     */
    List<Integer> selectUserGroupIds(@Param("userId") Integer userId);

    /**
     * 通过id获得分组个数
     *
     * @param id id
     * @return 个数
     */
    int selectCountById(@Param("id") Integer id);

    /**
     * 检查用户是否在该名称的分组里
     *
     * @param userId    用户id
     * @param groupName 分组名
     */
    int selectCountUserByUserIdAndGroupName(@Param("userId") Integer userId, @Param("groupName") String groupName);
}
