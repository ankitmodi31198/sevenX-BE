package com.backend.sevenX.security;

import com.backend.sevenX.data.model.Users;
import com.backend.sevenX.repository.UsersRepo;
import com.backend.sevenX.utills.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsersRepo usersRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
/*
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
*/

        String header = req.getHeader(Constant.JwtConst.KEY_AUTHORIZATION);
        String username = null;
        String authToken = null;

        if (header != null)
        {
            authToken =header;
            try {
                 username = jwtTokenUtil.getUsernameFromToken(authToken);
                Users admin = usersRepo.findByUsernameAndDeletedAtIsNull(username);
                // User is guest Type
              if(admin!=null) {
                    boolean tokenVerified = false;
                    if(authToken.equals(admin.getJwtToken())){
                            tokenVerified = true;
                    }else {
                        username = null;
                    }
                }

            } catch (Exception e) {
                logger.error("an error occured during getting username from token", e);
            }

        } else {
            logger.warn("couldn't find authorization string, will ignore the header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //if (jwtTokenUtil.validateToken(authToken, userDetails))
            // {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            logger.info("authenticated user " + username + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //}
        }
        chain.doFilter(req, res);
    }
}