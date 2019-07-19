# spring-mvc-logger
This library is used to log inbound and outbound requests for javax servlets

## Using example for Spring MVC
Add dependency in pom.xml
```xml
<dependency>
    <groupId>com.github.yegorbabarykin</groupId>
    <artifactId>spring-mvc-logger</artifactId>
    <version>1.0</version>
</dependency>
```
Or in gradle
```groovy
compile group: 'com.github.yegorbabarykin', name: 'spring-mvc-logger', version: '1.0'
```

Add new configuration for Spring MVC
```java
@Configuration
public class LoggingConfiguration implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<WrapHttpRequestResponseFilter> loggingFilter() {
        FilterRegistrationBean<WrapHttpRequestResponseFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new WrapHttpRequestResponseFilter());
        registrationBean.addUrlPatterns("*");
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggingInterceptor());
    }
}
```