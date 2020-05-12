package cn.kgc.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class RoleVo implements Serializable {
    private Integer id;
    private String roleName;
    private String roleDesc;
    private String [] auths;
 }
