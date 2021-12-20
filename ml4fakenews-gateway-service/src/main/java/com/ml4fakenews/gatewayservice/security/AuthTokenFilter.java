package com.ml4fakenews.gatewayservice.security;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowired
//    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("NO JESTETM w dofilterinternal");
        try{
            System.out.println("przed jwt");
            String jwt = parseJwt(httpServletRequest);
            System.out.println("po jwt");
           if(jwt != null && jwtTokenUtil.validateJwtToken(jwt)){
                String username  = jwtTokenUtil.getUsernameByToken(jwt);
                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<String> response
                        = restTemplate.getForEntity("http://accounts-service:3001/username/"+username, String.class);
                ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

                JSONObject json = new JSONObject(response.getBody());
                JSONArray array = json.getJSONArray("authorities");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject role  = array.getJSONObject(i);
                    authorities.add(new SimpleGrantedAuthority(role.getString("authority")));
                }
                //UserDetails UserDetails = new RestTemplate().getForObject(, UserDetails.class);
                //String username = "sada";
               // String authorities = null;
                System.out.println("no wrocil request");
                //UserDetails UserDetails = userService.loadUserByUsername(jwtTokenUtil.getUsernameByToken(jwt));
                UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );
                passwordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
            }
        }catch (Exception e){
            logger.error("User authentication setting error: {}", e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public String parseJwt(HttpServletRequest httpServletRequest) {
        String headerAuthentication = httpServletRequest.getHeader("Authorization");
        if(StringUtils.hasText(headerAuthentication) && headerAuthentication.startsWith("Bearer ")){
            return headerAuthentication.substring(7);
        }
        return null;
    }
}
