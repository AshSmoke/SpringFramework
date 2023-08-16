package cn.ashsmoke.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("cn.ashsmoke")
@EnableAspectJAutoProxy
public class SpringConfig {
}
