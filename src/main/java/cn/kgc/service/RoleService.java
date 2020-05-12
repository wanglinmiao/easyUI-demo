package cn.kgc.service;

import cn.kgc.entities.RoleInfo;
import cn.kgc.vo.RoleVo;
import com.github.pagehelper.Page;

import java.util.List;

public interface RoleService {
     void deleteById(String[] arrIds);
    Page<List<RoleInfo>> getRoleAll(Integer pageNumber, Integer pageSize, String roleName);
    void addRole(RoleVo role);
    RoleInfo getRoleById(Integer id);
    String[] getMenuIdByRoleId(Integer id);
    void updateRole(RoleVo param);

    List<RoleInfo> getAllRole();
}
