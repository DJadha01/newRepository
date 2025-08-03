package com.Security;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.Entity.User;
import com.Repository.UserRepo;
import com.Service.JwtService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepo userRepository;
	@Autowired
	private final JwtService jwtService;
	@Autowired
	private final UserDetailsService userDetailsService;

//	@Autowired
//	private final TokenBlacklistService blacklistService;
	@Autowired
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			log.debug("Authentication header : {}", authHeader);
			log.warn("Authentication header is null/non-bearer");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtService.extractUsername(jwt);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			//&& !blacklistService.isBlacklisted(userEmail);
			if (userEmail != null && authentication == null) {
				log.info("No existing authentication header");
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
				if (jwtService.isTokenValid(jwt, (CustomeUserService) userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					log.warn("Token has expired");
					User user = userRepository.findByEmail(userEmail)
							.orElseThrow(() -> new UsernameNotFoundException(userEmail));
//					user.setBlacklisted(true);
					userRepository.save(user);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}
	}

}