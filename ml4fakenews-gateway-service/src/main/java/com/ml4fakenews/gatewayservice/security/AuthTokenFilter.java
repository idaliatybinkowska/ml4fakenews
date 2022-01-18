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

        try{

            String jwt = parseJwt(httpServletRequest);

           if(jwt != null && jwtTokenUtil.validateJwtToken(jwt)){
                String username  = jwtTokenUtil.getUsernameByToken(jwt);
                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<String> response
                        = restTemplate.getForEntity("http://accounts-service:3001/username/"+username, String.class);
                ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

                JSONObject json = new JSONObject(response.getBody());
                JSONArray array = json.getJSONArray("authorities");
                boolean isProd = false;
               boolean isBusinessClient = false;

               for (int i = 0; i < array.length(); i++) {
                   String role  = array.getString(i);
                   if(role.equals("ROLE_BUSINESS")) {
                       isBusinessClient = true;
                   }
                   authorities.add(new SimpleGrantedAuthority(role));
               }

               if(!isBusinessClient && isProd) {
                   if(httpServletRequest.getHeader("referer") == null || httpServletRequest.getHeader("referer").contains("ml4fakenews-website")) {
                       System.out.println("Ten pan nie moze z tego punktu pytac");
                       throw new Exception("Brak uprawnień do korzystania z API");
                   }

               }
                //UserDetails UserDetails = new RestTemplate().getForObject(, UserDetails.class);
                //String username = "sada";
               // String authorities = null;

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
