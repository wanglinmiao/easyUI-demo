package cn.kgc.service;

import cn.kgc.entities.MenuInfo;

import java.util.List;

public interface MenuService {
    List<MenuInfo> getMenuListByPid(Integer pid);
}
