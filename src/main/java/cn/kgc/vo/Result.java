package cn.kgc.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
}
