package com.lyn.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lyn.model.UserDO;
import com.lyn.model.GroupDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息 view object
 *
 *  
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 工号
     */
    private String code;

    /**
     * 分组
     */
    private List<GroupDO> groups;

    public UserInfoVO(UserDO user, List<GroupDO> groups) {
        BeanUtil.copyProperties(user, this);
        this.groups = groups;
    }
}
