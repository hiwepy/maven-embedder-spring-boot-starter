# maven-embedder-spring-boot-starter
Spring Boot Starter For Maven Embedder

### 说明


 > 基于 maven-embedder 的 Spring Boot Starter 实现

1. 整合 maven-embedder 实现命令行程序内操作，需要主机安装配置Maven环境

### Maven

``` xml
<dependency>
	<groupId>io.github.easy4j</groupId>
	<artifactId>maven-embedder-spring-boot-starter</artifactId>
	<version>${project.version}</version>
</dependency>
```

### Sample

```java


import javax.annotation.PostConstruct;

import org.apache.maven.spring.boot.ext.MavenCliTemplate;
import org.apache.maven.spring.boot.ext.MavenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	
	@Autowired
	private MavenCliTemplate mavenCliTemplate;
	
	@PostConstruct
	private void init() {
		
		mavenCliTemplate.install(MavenResource.parse("D:\\p6spy-3.8.1.jar", "p6spy:p6spy:3.8.1-xx"));
		
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}

```
