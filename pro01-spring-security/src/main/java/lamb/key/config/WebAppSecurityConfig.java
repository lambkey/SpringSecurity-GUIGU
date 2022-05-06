package lamb.key.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author JoinYang
 * @date 2022/5/4 17:59
 */
// 将当前类标记为配置类
@Configuration
// 启用环境下权限控制功能
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService adminServiceImpl;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 基于内存
        /*auth.inMemoryAuthentication()
                .withUser("lamb")
                .password("123")
                .roles("师兄")                            // 添加角色时，会自动加上ROLE_前缀
                .and()
                .withUser("tom")
                .password("1234")
                .authorities("师父");*/

        auth.userDetailsService(adminServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("----------------"+dataSource+"--------------");
        http.authorizeRequests()
                .antMatchers("/index.jsp","/layui/**","/do/login")
                .permitAll()

                .antMatchers("/level1/**")   // 访问资源 角色要求
                .hasRole("师兄")                         // 添加角色时，会自动加上ROLE_前缀

                .antMatchers("/level2/**")  // 访问资源 权限要求
                .hasAuthority("师父")

                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/index.jsp")               // 访问任何没有开放的资源都会跳转到此页面
                .loginProcessingUrl("/do/login")       // 登录表单提交路径
                .defaultSuccessUrl("/main.html")       // 默认登陆成功后跳转的页面

                .and()
                .logout()
                .logoutUrl("/do/logout")               // 退出登录的执行路径
                .logoutSuccessUrl("/index.jsp")        // 退出成功后跳转的目标页面

                .and()
                .exceptionHandling()                   // 指定异常处理器
                .accessDeniedPage("/to/no/auth/page.html") //访问没有权限、被拒绝时显示页面异常

                .and()
                .rememberMe()                          // 开启记住我的功能
                .tokenRepository(persistentTokenRepository())  // 使记住我的功能不依赖缓存，依赖数据库，这样服务重启时，记住我的功能还可以实现



                .and()
                .csrf()
                .disable();

    }

    //静态文件一定要把name=“remember-me”
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //初始化会在数据库创建一张表，这个只能运行一次
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
