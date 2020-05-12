package cn.kgc.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="role_info")
public class RoleInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roleName;
    private String roleDesc;
    //@JsonFormat(pattern ="yyyy-MM-dd HH:mm;ss", timezone = "GMT+8")
    private Date fcd;
   // @JsonFormat(pattern ="yyyy-MM-dd HH:mm;ss", timezone = "GMT+8")
    private Date lcd;
}
