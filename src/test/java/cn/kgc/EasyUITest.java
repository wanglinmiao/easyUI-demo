package cn.kgc;

import cn.kgc.entities.UserInfo;
import cn.kgc.mapper.UserMapper;
import cn.kgc.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EasyUITest {
    @Resource
    UserService userService;
    @Test
    public void test1(){
        UserInfo admin = userService.login("admin", "123456");
        System.out.println(admin);
    }
    /*@Resource
    private UserMapper userMapper;
    @Test
    public void test(){
        List<UserInfo> list = userMapper.selectAll();
        System.out.println(list);
    }*/
}
