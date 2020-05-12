package cn.kgc.service;

import cn.kgc.entities.UserInfo;
import cn.kgc.vo.UserVo;
import com.github.pagehelper.Page;

import java.util.List;

public interface UserService {
    UserInfo login(String username,String psw);
    Page<List<UserInfo>> getAllUser(Integer pageNumber, Integer pageSize, String username);
    void addUser(UserVo userVo);
    void updateUserById(UserVo userVo);
    UserInfo getUserById(Integer id);
    String[] getRoleId(Integer id);
    void deleteUserId(String[] auths);
}
