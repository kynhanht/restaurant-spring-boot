package fu.rms.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.exception.AuthenException;
import fu.rms.request.LoginRequest;
import fu.rms.respone.LoginRespone;
import fu.rms.security.service.JwtUserDetails;
import fu.rms.utils.JwtTokenUtils;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class JwtAuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public LoginRespone login(@RequestBody @Valid LoginRequest loginRequest) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword()));

			JwtUserDetails jwtUserDetail = (JwtUserDetails) authentication.getPrincipal();

			// get token
			String token = JwtTokenUtils.generateJwtToken(jwtUserDetail);
			// get staffId
			Long staffId = jwtUserDetail.getId();
			// get code
			String staffCode = jwtUserDetail.getCode();
			// get role
			@SuppressWarnings("unchecked")
			List<GrantedAuthority> authorities = (List<GrantedAuthority>) jwtUserDetail.getAuthorities();
			List<String> roles = authorities.stream().map((authority) -> authority.getAuthority())
					.collect(Collectors.toList());

			LoginRespone loginRespone = new LoginRespone(token, staffId, staffCode, roles.get(0));

			return loginRespone;
		} catch (DisabledException e) {
			logger.error(e.getMessage());
			throw new AuthenException(MessageErrorConsant.ERROR_USER_NOT_ACTIVED);
		} catch (BadCredentialsException e) {
			logger.error(e.getMessage());
			throw new AuthenException(MessageErrorConsant.ERROR_USER_LOGIN_FAILED);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new AuthenException(MessageErrorConsant.ERROR_USER_UNAUTHORIZED);
		}

	}

	@PostMapping("/pre-login")
	public LoginRespone preLogin(HttpServletRequest request) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()) {
				throw new AuthenException(MessageErrorConsant.ERROR_USER_UNAUTHORIZED);
			}else {
				JwtUserDetails jwtUserDetail = (JwtUserDetails) authentication.getPrincipal();

				// get token
				String token=request.getHeader("token");
				// get staffId
				Long staffId = jwtUserDetail.getId();
				// get code
				String staffCode = jwtUserDetail.getCode();
				// get role
				@SuppressWarnings("unchecked")
				List<GrantedAuthority> authorities = (List<GrantedAuthority>) jwtUserDetail.getAuthorities();
				List<String> roles = authorities.stream().map((authority) -> authority.getAuthority())
						.collect(Collectors.toList());

				LoginRespone loginRespone = new LoginRespone(token, staffId, staffCode, roles.get(0));

				return loginRespone;
			}		

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new AuthenException(MessageErrorConsant.ERROR_USER_UNAUTHORIZED);
		}
	}

}
