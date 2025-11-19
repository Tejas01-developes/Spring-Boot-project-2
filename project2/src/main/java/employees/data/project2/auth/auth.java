package employees.data.project2.auth;
import employees.data.project2.filter.filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class auth {
    @Autowired
    private filter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     return    http
                .csrf(csrf->csrf.disable())
             .authorizeHttpRequests(auth->auth.requestMatchers("/info/makeaccount","/info/login")
                     .permitAll().anyRequest().authenticated())
                .cors(cors->{})
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
// .authorizeHttpRequests(auth->auth.requestMatchers("/info/makeaccount","/info/login").permitAll().anyRequest().authenticated())
// .authorizeHttpRequests(auth->auth.anyRequest().permitAll())