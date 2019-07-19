# spring-mvc-logger
This library is used to log inbound and outbound requests for javax servlets

## Using example for Spring MVC
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