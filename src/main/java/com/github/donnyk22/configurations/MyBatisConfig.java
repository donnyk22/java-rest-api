package com.github.donnyk22.configurations;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

// Scan MyBatis mapper interfaces, both hardcoded and auto-generated.
// @MapperScan is recursive, so this single base package also covers the
// It is safe even before generation: @MapperScan tolerates an empty/nonexistent sub-package.
@Configuration
@MapperScan("com.github.donnyk22.repositories.mybatis")
public class MyBatisConfig {

}
