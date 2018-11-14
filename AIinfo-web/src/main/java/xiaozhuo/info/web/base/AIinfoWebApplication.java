package xiaozhuo.info.web.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("xiaozhuo.info.persist.mapper")
// 项目中对应的mapper类的路径
public class AIinfoWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIinfoWebApplication.class, args);
	}

}