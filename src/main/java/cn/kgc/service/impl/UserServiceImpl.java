package cn.kgc.service.impl;

import cn.kgc.entities.RoleInfo;
import cn.kgc.entities.UserInfo;
import cn.kgc.entities.UserRole;
import cn.kgc.mapper.UserMapper;
import cn.kgc.mapper.UserRoleMapper;
import cn.kgc.service.UserService;
import cn.kgc.vo.UserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Override
    public UserInfo login(String username, String psw) {
        UserInfo param=new UserInfo();
        param.setUsername(username);
        param.setPsw(psw);
        UserInfo user = userMapper.selectOne(param);
        return user;
    }

    @Override
    public Page<List<UserInfo>> getAllUser(Integer pageNumber, Integer pageSize, String username) {
        PageHelper.startPage(pageNumber,pageSize);
        Example example=new Example(UserInfo.class);
        if (StringUtil.isNotEmpty(username)) {
            example.createCriteria().andLike("username", "%" + username + "%");
        }
        List<UserInfo> list = userMapper.selectByExample(example);
        Page page= (Page) list;
        return page;
    }

    @Override
    public void addUser(UserVo userVo) {
        UserInfo userInfo=new UserInfo();
        BeanUtils.copyProperties(userVo,userInfo);
        userMapper.insert(userInfo);
        String[] auths=userVo.getAuths();
        for (String auth:auths){
            UserRole userRole = new UserRole();
            userRole.setRoleId(Integer.parseInt(auth));
            userRole.setUserId(userInfo.getId());
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void updateUserById(UserVo userVo) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userVo,userInfo);
        userMapper.updateByPrimaryKey(userInfo);
        String[] auths=userVo.getAuths();
        UserRole userRole=new UserRole();
        userRole.setUserId(userVo.getId());
        userRoleMapper.delete(userRole);
        for(String s:auths){
            UserRole userRole1=new UserRole();
            userRole1.setUserId(userVo.getId());
            userRole1.setRoleId(Integer.parseInt(s));
            userRoleMapper.insert(userRole1);
        }
    }

    @Override
    public UserInfo getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public String[] getRoleId(Integer id) {
        UserRole userRole=new UserRole();
        userRole.setUserId(id);
        List<UserRole> select = userRoleMapper.select(userRole);
        String[] s=new String[select.size()];
        int index=0;
        for(UserRole u:select){
            s[index++]=u.getRoleId()+"";
        }
        return s;
    }

    @Override
    public void deleteUserId(String[] auths) {
        for(String s:auths){
            userMapper.deleteByPrimaryKey(s);
            UserRole userRole = new UserRole();
            userRole.setUserId(Integer.parseInt(s));
            userRoleMapper.delete(userRole);
        }
    }
}
