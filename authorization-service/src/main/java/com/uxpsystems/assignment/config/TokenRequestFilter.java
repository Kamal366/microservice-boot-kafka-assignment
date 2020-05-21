package com.uxpsystems.assignment.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uxpsystems.assignment.service.UserServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class TokenRequestFilter extends OncePerRequestFilter {

	private static final String JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING = "JWT Token does not begin with Bearer String";

	private static final String JWT_TOKEN_HAS_EXPIRED = "JWT Token has expired";

	private static final String UNABLE_TO_GET_JWT_TOKEN = "Unable to get JWT Token";

	private static final String BEARER = "Bearer ";

	private static final String AUTHORIZATION = "Authorization";
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private TokenUtil TokenUtil;

	/**
	 *Filter the valid request Auth data
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader(AUTHORIZATION); //Auth data

		String username = null;
		String jwtToken = null;
		// bearer Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = TokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println(UNABLE_TO_GET_JWT_TOKEN);
			} catch (ExpiredJwtException e) {
				System.out.println(JWT_TOKEN_HAS_EXPIRED);
			}
		} else {
			logger.warn(JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING);
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = userServiceImpl.loadUserByUsername(username);

			// authentication
			if (TokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// that the current user is authenticated. 
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}