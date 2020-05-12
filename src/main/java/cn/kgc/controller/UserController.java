package cn.kgc.controller;

import ch.qos.logback.core.db.dialect.DBUtil;
import cn.kgc.entities.MenuInfo;
import cn.kgc.entities.RoleInfo;
import cn.kgc.entities.UserInfo;
import cn.kgc.entities.UserRole;
import cn.kgc.mapper.UserMapper;
import cn.kgc.service.MenuService;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserService;
import cn.kgc.vo.Result;
import cn.kgc.vo.RoleVo;
import cn.kgc.vo.UserVo;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleService roleService;

    @GetMapping("/login/view")
    public String loginUI() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<Object> login(UserInfo param, HttpSession Session) {
        Result<Object> rs = new Result<>();
        UserInfo user = userService.login(param.getUsername(), param.getPsw());
        if (user == null) {
            rs.setCode(20001);
            rs.setMsg("用户名或密码错误");
        } else {
            rs.setCode(20000);
            rs.setMsg("登陆成功");
            Session.setAttribute("userInfo", user);
        }
        return rs;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/menu")
    @ResponseBody
    public Result<List<MenuInfo>> getMenuListByPid(@RequestParam(name = "id", required = false) Integer pid, HttpSession session) {
        if (pid==null){
            pid=0;
        }
        Result<List<MenuInfo>> rs = new Result<>();
//        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
//        String[] roleId = userService.getRoleId(userInfo.getId());
//        for (String s : roleId) {
//            String[] menuIdByRoleId = roleService.getMenuIdByRoleId(Integer.parseInt(s));
//            for (String ss : menuIdByRoleId) {
//            }
//        }
        try {
            List<MenuInfo> menuListByPid = menuService.getMenuListByPid(pid);
            rs.setCode(20000);
            rs.setMsg("查询成功");
            rs.setData(menuListByPid);
        } catch (Exception e) {
            rs.setCode(20002);
            rs.setMsg("菜单查询失败" + e.getMessage());
        }
        return rs;
    }

    @GetMapping("/role/view")
    public String toRoleUI() {
        return "role";
    }

    @GetMapping("/user/view")
    public String toUserUI() {
        return "user";
    }

    @RequestMapping("/role")
    @ResponseBody
    public Result<Map<String, Object>> getRoleList(@RequestParam("page") Integer pageNumber, @RequestParam("rows") Integer pageSize,
                                                   @RequestParam(value = "roleName", required = false) String roleName) {
        System.out.println(roleName);
        Result<Map<String, Object>> rs = new Result<Map<String, Object>>();
        Page<List<RoleInfo>> roleList = roleService.getRoleAll(pageNumber, pageSize, roleName);
        rs.setCode(20000);
        rs.setMsg("查询成功");
        Map<String, Object> data = new HashMap<>();
        data.put("total", roleList.getTotal());
        data.put("rows", roleList.getResult());
        rs.setData(data);
        return rs;
    }

    @PostMapping("/role/save")
    @ResponseBody
    public Result<Object> addRole(RoleVo param) {
        Result<Object> rs = new Result<>();
        if (param.getId() != null) {
            roleService.updateRole(param);
            rs.setCode(20000);
            rs.setMsg("修改角色成功");
            return rs;
        } else {
            try {
                roleService.addRole(param);
                rs.setCode(20000);
                rs.setMsg("新增角色成功");
            } catch (Exception e) {
                rs.setCode(20002);
                rs.setMsg("新增角色失败" + e.getMessage());
            }
            return rs;
        }
    }

    @RequestMapping("/role/{id}")
    @ResponseBody
    public RoleVo toRoleUpdate(@PathVariable("id") Integer id) {
        RoleVo roleVo = new RoleVo();
        RoleInfo role = roleService.getRoleById(id);
        BeanUtils.copyProperties(role, roleVo);
        String[] auths = roleService.getMenuIdByRoleId(id);
        roleVo.setAuths(auths);
        return roleVo;
    }

    @PostMapping("/role/del")
    @ResponseBody
    public Result<Object> deleteRoleById(@RequestParam("ids") String ids) {
        String[] arrIds = ids.split(",");
        roleService.deleteById(arrIds);
        Result<Object> rs = new Result<>();
        rs.setCode(20000);
        rs.setMsg("删除成功");
        return rs;
    }

    @RequestMapping("/user")
    @ResponseBody
    public Result<Map<String, Object>> getUserList(@RequestParam("page") Integer pageNumber, @RequestParam("rows") Integer pageSize,
                                                   @RequestParam(value = "username", required = false) String username) {
        Result<Map<String, Object>> rs = new Result<Map<String, Object>>();
        Page<List<UserInfo>> userlist = userService.getAllUser(pageNumber, pageSize, username);
        rs.setCode(20000);
        rs.setMsg("查询成功");
        Map<String, Object> data = new HashMap<>();
        data.put("total", userlist.getTotal());
        data.put("rows", userlist.getResult());
        rs.setData(data);
        return rs;
    }

    @PostMapping("/user/role")
    @ResponseBody
    public Result<Object> getAllRole() {
        Result<Object> rs = new Result<>();
        List<RoleInfo> role = roleService.getAllRole();
        System.out.println(role);
        rs.setCode(20000);
        rs.setMsg("查找成功");
        rs.setData(role);
        return rs;
    }

    @RequestMapping("/user/{id}")
    @ResponseBody
    public UserVo toupdateUserById(@PathVariable("id") Integer id) {
        UserVo vo = new UserVo();
        UserInfo userInfo = userService.getUserById(id);
        BeanUtils.copyProperties(userInfo, vo);
        String[] Auths = userService.getRoleId(id);
        vo.setAuths(Auths);
        return vo;
    }

    @PostMapping("/user/add")
    @ResponseBody
    public Result<Object> addUserList(UserVo userVo) {
        Result<Object> rs = new Result<>();
        if (userVo.getId() != null) {
            userService.updateUserById(userVo);
            rs.setCode(20000);
            rs.setMsg("修改成功");
        } else {
            userService.addUser(userVo);
            rs.setCode(20000);
            rs.setMsg("新增成功");
        }
        return rs;
    }

    @PostMapping("/user/del")
    @ResponseBody
    public Result<Object> deleteUserById(@RequestParam("ids") String ids) {
        System.out.println(ids);
        String[] auths = ids.split(",");
        userService.deleteUserId(auths);
        Result<Object> rs = new Result<>();
        rs.setCode(20000);
        rs.setMsg("删除成功");
        return rs;
    }
}
