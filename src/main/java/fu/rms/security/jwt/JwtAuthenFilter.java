package fu.rms.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import fu.rms.security.service.JwtUserDetails;
import fu.rms.security.service.JwtUserDetailsService;
import fu.rms.utils.JwtTokenUtils;

public class JwtAuthenFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenFilter.class);

	@Autowired
	private JwtUserDetailsService jwtUserDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			
			// get token from client
			HttpServletRequest rq = (HttpServletRequest) request;
			String token = rq.getHeader("token");
			// check valid token
			if (StringUtils.hasText(token) && JwtTokenUtils.validateJwtToken(token)) {
				String username = JwtTokenUtils.getUsernameByJwtToken(token);
				JwtUserDetails jwtUserDetail = jwtUserDetailService.loadUserByUsername(username);
				// check user exists
				if (jwtUserDetail != null ) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							jwtUserDetail, null, jwtUserDetail.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);

		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}
}
