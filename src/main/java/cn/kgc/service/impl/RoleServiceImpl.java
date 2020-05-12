package cn.kgc.service.impl;

import cn.kgc.entities.MenuInfo;
import cn.kgc.entities.RoleInfo;
import cn.kgc.entities.RoleMenu;
import cn.kgc.mapper.MenuMapper;
import cn.kgc.mapper.RoleMapper;
import cn.kgc.mapper.RoleMenuMapper;
import cn.kgc.service.MenuService;
import cn.kgc.service.RoleService;
import cn.kgc.vo.RoleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void deleteById(String[] arrIds) {
        //删除角色
        for(String roleId:arrIds){
            roleMapper.deleteByPrimaryKey(roleId);
            //删除角色权限
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(Integer.parseInt(roleId));
            roleMenuMapper.delete(roleMenu);
        }

    }

    @Override
    public Page<List<RoleInfo>> getRoleAll(Integer pageNumber, Integer pageSize, String roleName) {
        PageHelper.startPage(pageNumber,pageSize);
        Example example=new Example(RoleInfo.class);
        if (StringUtil.isNotEmpty(roleName)) {
            example.createCriteria().andLike("roleName", "%" + roleName + "%");
        }
        List<RoleInfo> list = roleMapper.selectByExample(example);
        Page page= (Page) list;
        return page;
    }

    @Override
    @Transactional
    public void addRole(RoleVo role) {
        //将提交的RoleVo数据整合到RoleInfo中
        RoleInfo roleInfo=new RoleInfo();
        roleInfo.setFcd(new Date());
        roleInfo.setLcd(new Date());
        BeanUtils.copyProperties(role,roleInfo);
        roleMapper.insert(roleInfo);
        //拿去刚刚新增RoleInfo的id
        Integer roleId=roleInfo.getId();
        //获取导航栏的Id也就是menuID
        String[] auths=role.getAuths();
        //menu_info根据Id查询相应pid
        Example example=new Example(MenuInfo.class);
        example.createCriteria().andIn("id", Arrays.asList(auths));
        List<MenuInfo> menuInfos = menuMapper.selectByExample(example);
        //去重把menu_info的pid放入set
        Set<String> sAuth=new HashSet<>();
        for (MenuInfo m:menuInfos){
            sAuth.add(m.getPid()+"");
        }
        //合并去重把menu_info的id放入set
        sAuth.addAll(Arrays.asList(auths));
        //遍历set的id放入roleMenu表mid也就是menu_id
        for(String mid:sAuth){
            RoleMenu roleMenu=new RoleMenu();
            roleMenu.setMenuId(Integer.parseInt(mid));
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public RoleInfo getRoleById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public String[] getMenuIdByRoleId(Integer id) {
        RoleMenu param=new RoleMenu();
        param.setRoleId(id);
        List<RoleMenu> list = roleMenuMapper.select(param);
        if(list!=null){
            String[] auths=new String[list.size()];
             int index=0;
            for(RoleMenu roleMenu:list){
                auths[index++] = roleMenu.getMenuId()+"";
            }
            return auths;
        }

        return null;
    }

    @Override
    @Transactional
    public void updateRole(RoleVo param) {
        RoleInfo roleInfo = new RoleInfo();
        BeanUtils.copyProperties(param,roleInfo);
        roleInfo.setLcd(new Date());
        roleMapper.updateByPrimaryKeySelective(roleInfo);
        RoleMenu roleMenu=new RoleMenu();
        roleMenu.setRoleId(param.getId());
        roleMenuMapper.delete(roleMenu);
        String[] auths = param.getAuths();
        Example example=new Example(MenuInfo.class);
        example.createCriteria().andIn("id", Arrays.asList(auths));
        List<MenuInfo> menuInfos = menuMapper.selectByExample(example);
        //去重把menu_info的pid放入set
        Set<String> sAuth=new HashSet<>();
        for (MenuInfo m:menuInfos){
            sAuth.add(m.getPid()+"");
        }
        sAuth.addAll(Arrays.asList(auths));
        for(String mid:sAuth){
            RoleMenu roleMenu1=new RoleMenu();
            roleMenu1.setMenuId(Integer.parseInt(mid));
            roleMenu1.setRoleId(param.getId());
            roleMenuMapper.insert(roleMenu1);
        }
    }

    @Override
    public List<RoleInfo> getAllRole() {
        return roleMapper.selectAll();
    }
}
