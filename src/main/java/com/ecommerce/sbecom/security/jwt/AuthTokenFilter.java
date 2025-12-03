package com.ecommerce.sbecom.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
//        String jwt = jwtUtils.getJwtFromHeader(request);
        String jwt = jwtUtils.getJwtFromCookies(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}


// UserDetails: It allows you to retrieve core user information. It is an interface which provides core user information.
// It has a lot of methods, and it is basically in package spring security core. We can also create custom implementation
// of UserDetails. Reason for custom UserDetails - We have custom way of representing user in our application using model
// User. Now this user might extend in the future, or it might custom fields specific to your domain while UserDetail is
// a standard interface that is provided by spring security. There will be scenario where you have your own User model in
// that case you want to customize this UserDetails. Because you want to represent your User info with your own fields,
// custom attributes, custom logic, or you might have your own roles to add. So this is the reason why we would be
// customizing this interface. In future if your application grows, and we want to add more fields, or you scale further
// then your code should also be scalable. And you should have control over on what you want to represent and how you
// want to represent it. This is the main reason why companies/developers customize this interface.

// UserDetailsService: This interface provides mechanism to retrieve the user data based on the username.And it is
// responsible for loading user specific data from wherever you want to load it from. It has method like loadUserByUsername.
// And this method output you will be assigning it to UserDetails (interface) object. Tomorrow if you have your own custom
// datasource, and we might want to customize this, we might want to adapt retrieving user details functionality specific
// to our domain in our future, we might want to implement different query or custom error handling so for all of this
// reason we will be making use of this interface and customize things further.

// Cookie Based Authentication: Bearer tokens need to be added explicitly to the HTTP request. Browser will automatically send cookies. User tries to
// login, token is generated, token is issued to user as a cookie, JWT cookie sent in API requests, token validated,
// request authorized if valid, else error.
