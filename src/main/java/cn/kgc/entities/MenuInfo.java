package cn.kgc.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name="menu_info")
public class MenuInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="menutext")
    private String text;
    private String state;
    private String url;
    private Integer pid;
    private String iconCls;
}
