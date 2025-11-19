package employees.data.project2.filter;

import employees.data.project2.refreshtoken.refreshtoken;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class filter implements Filter {
    @Autowired
    private refreshtoken refreshtoken;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse) servletResponse;

        String path=req.getRequestURI();
        if(path.contains("/info/makeaccount") || path.contains("/info/login")){
            filterChain.doFilter(req,resp);
            return;
        }

        if("OPTIONS".equalsIgnoreCase(req.getMethod())){
            resp.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(req,resp);
            return;
        }

        Cookie[] cookies=req.getCookies();
        String accesstokens=null;
        String refreshtokenss=null;
        if(cookies!=null){
            for (Cookie c:cookies){
                if("accesstokenn".equals(c.getName())){
                    accesstokens=c.getValue();
                } else if ("refreshtokenn".equals(c.getName())) {
                    refreshtokenss=c.getValue();
                }
            }
        }

            if(accesstokens!=null && refreshtoken.verifytoken(accesstokens)){
                String username=refreshtoken.extractusername(accesstokens);
                String role=refreshtoken.extractrole(accesstokens);
                req.setAttribute("role",role);
                req.setAttribute("username",username);
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,null, List.of(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(req,resp);
                return;
            }

            if(refreshtokenss!=null && refreshtoken.verifytoken(refreshtokenss)){
                String username=refreshtoken.extractusername(refreshtokenss);
                String roles=refreshtoken.extractrole(refreshtokenss);
                req.setAttribute("role",roles);
                String newaccesstoken=refreshtoken.accesstoken(username,roles);

                Cookie accesscookie=new Cookie("accesstokenn",newaccesstoken);
                accesscookie.setHttpOnly(true);
                accesscookie.setPath("/");
                accesscookie.setMaxAge(15 * 60);
                resp.addCookie(accesscookie);
                req.setAttribute("username",username);
                filterChain.doFilter(req,resp);
                return;

            }
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.getWriter().write("Unauthorized: invalid or missing tokens");


    }
}
