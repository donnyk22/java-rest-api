package com.github.donnyk22.configurations;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

// Scan MyBatis mapper interfaces, both hardcoded and auto-generated.
// The "generated.*" package may not exist yet (before running "mvn mybatis-generator:generate") — @MapperScan is safe if the package is empty/nonexistent.
@Configuration
@MapperScan({
        "com.github.donnyk22.repositories.mybatis",
        "com.github.donnyk22.generated.mybatis.mapper"
})
public class MyBatisConfig {

}
