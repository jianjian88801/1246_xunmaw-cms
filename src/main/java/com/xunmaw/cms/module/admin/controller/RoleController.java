package com.xunmaw.cms.module.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.shiro.MyShiroRealm;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.model.Permission;
import com.xunmaw.cms.module.admin.model.Role;
import com.xunmaw.cms.module.admin.model.User;
import com.xunmaw.cms.module.admin.service.PermissionService;
import com.xunmaw.cms.module.admin.service.RoleService;
import com.xunmaw.cms.module.admin.vo.PermissionTreeListVo;
import com.xunmaw.cms.module.admin.vo.base.PageResultVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 后台角色配置
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@Controller
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;
    private final MyShiroRealm myShiroRealm;


    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "role/form";
    }

    /*角色列表数据*/
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo pageRoles(Role role, Integer pageNumber, Integer pageSize) {
        try {
            IPage<Role> rolePage = roleService.selectRoles(role, pageNumber, pageSize);
            return ResultUtil.table(rolePage.getRecords(), rolePage.getTotal());
        } catch (Exception e) {
            log.error(String.format("RoleController.loadRoles%s", e));
            throw e;
        }
    }

    /*新增角色*/
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo addRole(Role role) {
        try {
            int a = roleService.insert(role);
            if (a > 0) {
                return ResultUtil.success("添加角色成功");
            } else {
                return ResultUtil.error("添加角色失败");
            }
        } catch (Exception e) {
            log.error(String.format("RoleController.addRole%s", e));
            throw e;
        }
    }

    /*删除角色*/
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo deleteRole(String roleId) {
        if (roleService.findByRoleId(roleId).size() > 0) {
            return ResultUtil.error("删除失败,该角色下存在用户");
        }
        List<String> roleIdsList = Collections.singletonList(roleId);
        int a = roleService.updateStatusBatch(roleIdsList, CoreConst.STATUS_INVALID);
        if (a > 0) {
            return ResultUtil.success("删除角色成功");
        } else {
            return ResultUtil.error("删除角色失败");
        }
    }

    /*批量删除角色*/
    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo batchDeleteRole(@RequestParam("ids[]") String[] ids) {
        List<String> roleIdsList = Arrays.asList(ids);
        if (CollUtil.isNotEmpty(roleService.findByRoleIds(roleIdsList))) {
            return ResultUtil.error("删除失败,选择的角色下存在用户");
        }
        int a = roleService.updateStatusBatch(roleIdsList, CoreConst.STATUS_INVALID);
        if (a > 0) {
            return ResultUtil.success("删除角色成功");
        } else {
            return ResultUtil.error("删除角色失败");
        }
    }

    /*编辑角色详情*/
    @GetMapping("/edit")
    public String detail(Model model, Integer roleId) {
        Role role = roleService.findById(roleId);
        model.addAttribute("role", role);
        return CoreConst.ADMIN_PREFIX + "role/form";
    }

    /*编辑角色*/
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo editRole(@ModelAttribute("role") Role role) {
        int a = roleService.updateByRoleId(role);
        if (a > 0) {
            return ResultUtil.success("编辑角色成功");
        } else {
            return ResultUtil.error("编辑角色失败");
        }
    }

    /*分配权限列表查询*/
    @PostMapping("/assign/permission/list")
    @ResponseBody
    public List<PermissionTreeListVo> assignRole(String roleId) {
        List<PermissionTreeListVo> listVos = new ArrayList<>();
        List<Permission> allPermissions = permissionService.selectAll(CoreConst.STATUS_VALID);
        List<Permission> hasPermissions = roleService.findPermissionsByRoleId(roleId);
        for (Permission permission : allPermissions) {
            PermissionTreeListVo vo = new PermissionTreeListVo();
            vo.setId(permission.getId());
            vo.setPermissionId(permission.getPermissionId());
            vo.setName(permission.getName());
            vo.setParentId(permission.getParentId());
            for (Permission hasPermission : hasPermissions) {
                //有权限则选中
                if (hasPermission.getPermissionId().equals(permission.getPermissionId())) {
                    vo.setChecked(true);
                    break;
                }
            }
            listVos.add(vo);
        }
        return listVos;
    }


    /*分配权限*/
    @PostMapping("/assign/permission")
    @ResponseBody
    public ResponseVo assignRole(String roleId, String permissionIdStr) {
        List<String> permissionIdsList = new ArrayList<>();
        if (StrUtil.isNotBlank(permissionIdStr)) {
            String[] permissionIds = permissionIdStr.split(",");
            permissionIdsList = Arrays.asList(permissionIds);
        }
        try {
            roleService.addAssignPermission(roleId, permissionIdsList);
            /*重新加载角色下所有用户权限*/
            List<User> userList = roleService.findByRoleId(roleId);
            if (!userList.isEmpty()) {
                List<String> userIds = new ArrayList<>();
                for (User user : userList) {
                    userIds.add(user.getUserId());
                }
                myShiroRealm.clearAuthorizationByUserId(userIds);
            }
            return ResultUtil.success("分配权限成功");
        } catch (Exception e) {
            return ResultUtil.error("分配权限失败");
        }
    }

}
