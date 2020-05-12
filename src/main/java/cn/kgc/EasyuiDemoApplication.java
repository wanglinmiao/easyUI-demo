package cn.kgc;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.kgc.mapper")
@EnableTransactionManagement
public class EasyuiDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyuiDemoApplication.class);
    }
}
