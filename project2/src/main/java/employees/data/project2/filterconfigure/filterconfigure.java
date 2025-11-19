package employees.data.project2.filterconfigure;

import employees.data.project2.filter.filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class filterconfigure {
    @Autowired
    private filter filter;
    @Bean
    public FilterRegistrationBean<filter>registrationBean(filter filter){
        FilterRegistrationBean<filter>filterRegistrationBean=new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
