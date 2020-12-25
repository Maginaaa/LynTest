package com.lyn.bo;

import cn.hutool.core.bean.BeanUtil;
import com.lyn.model.PermissionDO;
import com.lyn.model.GroupDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 简单随风
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPermissionBO {
    private Integer id;

    private String name;

    private String info;

    private List<PermissionDO> permissions;

    public GroupPermissionBO(GroupDO group, List<PermissionDO> permissions) {
        BeanUtil.copyProperties(group, this);
        this.permissions = permissions;
    }
}
