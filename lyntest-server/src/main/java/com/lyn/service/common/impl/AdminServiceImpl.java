package com.lyn.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn.bo.GroupPermissionBO;
import com.lyn.common.enumeration.GroupLevelEnum;
import com.lyn.mapper.common.GroupPermissionMapper;
import com.lyn.service.common.AdminService;
import com.lyn.service.common.GroupService;
import com.lyn.service.common.PermissionService;
import com.lyn.service.common.UserService;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import com.lyn.dto.admin.DispatchPermissionDTO;
import com.lyn.dto.admin.DispatchPermissionsDTO;
import com.lyn.dto.admin.NewGroupDTO;
import com.lyn.dto.admin.RemovePermissionsDTO;
import com.lyn.dto.admin.ResetPasswordDTO;
import com.lyn.dto.admin.UpdateGroupDTO;
import com.lyn.dto.admin.UpdateUserInfoDTO;
import com.lyn.model.GroupDO;
import com.lyn.model.GroupPermissionDO;
import com.lyn.model.PermissionDO;
import com.lyn.model.UserDO;
import com.lyn.model.UserIdentityDO;
import com.lyn.service.common.UserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * @author 
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private GroupPermissionMapper groupPermissionMapper;

    @Override
    public IPage<UserDO> getUserPageByGroupId(Integer groupId, Integer count, Integer page) {
        Page<UserDO> pager = new Page<>(page, count);
        IPage<UserDO> iPage;
        // 如果group_id为空，则以分页的形式返回所有用户
        if (groupId == null) {
            QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
            Integer rootUserId = userService.getRootUserId();
            wrapper.lambda().ne(UserDO::getId, rootUserId);
            iPage = userService.page(pager, wrapper);
        } else {
            iPage = userService.getUserPageByGroupId(pager, groupId);
        }
        return iPage;
    }

    @Override
    public boolean changeUserPassword(Integer id, ResetPasswordDTO dto) {
        throwUserNotExistById(id);
        return userIdentityService.changePassword(id, dto.getNewPassword());
    }

    @Transactional
    @Override
    public boolean deleteUser(Integer id) {
        throwUserNotExistById(id);
        boolean userRemoved = userService.removeById(id);
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, id);
        return userRemoved && userIdentityService.remove(wrapper);
    }

    @Override
    public boolean updateUserInfo(Integer id, UpdateUserInfoDTO validator) {
        List<Integer> newGroupIds = validator.getGroupIds();
        if (newGroupIds == null || newGroupIds.isEmpty()) {
            return false;
        }
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        boolean anyMatch = newGroupIds.stream().anyMatch(it -> it.equals(rootGroupId));
        if (anyMatch) {
            throw new ForbiddenException(10073);
        }
        List<Integer> existGroupIds = groupService.getUserGroupIdsByUserId(id);
        // 删除existGroupIds有，而newGroupIds没有的
        List<Integer> deleteIds = existGroupIds.stream().filter(it -> !newGroupIds.contains(it)).collect(Collectors.toList());
        // 添加newGroupIds有，而existGroupIds没有的
        List<Integer> addIds = newGroupIds.stream().filter(it -> !existGroupIds.contains(it)).collect(Collectors.toList());
        return groupService.deleteUserGroupRelations(id, deleteIds) && groupService.addUserGroupRelations(id, addIds);
    }

    @Override
    public IPage<GroupDO> getGroupPage(Integer page, Integer count) {
        IPage<GroupDO> iPage = groupService.getGroupPage(page, count);
        return iPage;
    }

    @Override
    public GroupPermissionBO getGroup(Integer id) {
        throwGroupNotExistById(id);
        return groupService.getGroupAndPermissions(id);
    }

    @Transactional
    @Override
    public boolean createGroup(NewGroupDTO dto) {
        throwGroupNameExist(dto.getName());
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        groupService.save(group);
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                    .map(id -> new GroupPermissionDO(group.getId(), id))
                    .collect(Collectors.toList());
            groupPermissionMapper.insertBatch(relations);
        }
        return true;
    }

    @Override
    public boolean updateGroup(Integer id, UpdateGroupDTO dto) {
        // bug 如果只修改info，不修改name，则name已经存在，此时不应该报错
        GroupDO exist = groupService.getById(id);
        if (exist == null) {
            throw new NotFoundException(10024);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwGroupNameExist(dto.getName());
        }
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        group.setId(id);
        return groupService.updateById(group);
    }

    @Override
    public boolean deleteGroup(Integer id) {
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        Integer guestGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);
        if (id.equals(rootGroupId)) {
            throw new ForbiddenException(10074);
        }
        if (id.equals(guestGroupId)) {
            throw new ForbiddenException(10075);
        }
        throwGroupNotExistById(id);
        return groupService.removeById(id);
    }

    @Override
    public boolean dispatchPermission(DispatchPermissionDTO dto) {
        GroupPermissionDO groupPermission = new GroupPermissionDO(dto.getGroupId(), dto.getPermissionId());
        return groupPermissionMapper.insert(groupPermission) > 0;
    }

    @Override
    public boolean dispatchPermissions(DispatchPermissionsDTO dto) {
        List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                .map(id -> new GroupPermissionDO(dto.getGroupId(), id))
                .collect(Collectors.toList());
        return groupPermissionMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean removePermissions(RemovePermissionsDTO dto) {
        return groupPermissionMapper.deleteBatchByGroupIdAndPermissionId(dto.getGroupId(), dto.getPermissionIds()) > 0;
    }

    @Override
    public List<GroupDO> getAllGroups() {
        QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        wrapper.lambda().ne(GroupDO::getId, rootGroupId);
        List<GroupDO> groups = groupService.list(wrapper);
        return groups;
    }

    @Override
    public List<PermissionDO> getAllPermissions() {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getMount, true);
        return permissionService.list(wrapper);
    }

    @Override
    public Map<String, List<PermissionDO>> getAllStructuralPermissions() {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getMount, true);
        List<PermissionDO> permissions = getAllPermissions();
        Map<String, List<PermissionDO>> res = new HashMap<>();
        permissions.forEach(permission -> {
            if (res.containsKey(permission.getModule())) {
                res.get(permission.getModule()).add(permission);
            } else {
                ArrayList<PermissionDO> t = new ArrayList<>();
                t.add(permission);
                res.put(permission.getModule(), t);
            }
        });
        return res;
    }

    private void throwUserNotExistById(Integer id) {
        boolean exist = userService.checkUserExistById(id);
        if (!exist) {
            throw new NotFoundException(10021);
        }
    }

    private void throwGroupNotExistById(Integer id) {
        boolean exist = groupService.checkGroupExistById(id);
        if (!exist) {
            throw new NotFoundException(10024);
        }
    }

    private void throwGroupNameExist(String name) {
        boolean exist = groupService.checkGroupExistByName(name);
        if (exist) {
            throw new ForbiddenException(10072);
        }
    }
}
