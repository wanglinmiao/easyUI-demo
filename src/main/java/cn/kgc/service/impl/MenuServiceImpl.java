package cn.kgc.service.impl;

import cn.kgc.entities.MenuInfo;
import cn.kgc.mapper.MenuMapper;
import cn.kgc.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper menuMapper;
    @Override
    public List<MenuInfo> getMenuListByPid(Integer pid) {
        MenuInfo mi = new MenuInfo();
        mi.setPid(pid);
        List<MenuInfo> list = menuMapper.select(mi);
        return list;
    }
}
