package fu.rms.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import fu.rms.security.service.JwtUserDetails;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		  if (authentication == null || !authentication.isAuthenticated()) {
		   return null;
		  }

		 String code= ((JwtUserDetails) authentication.getPrincipal()).getCode();
		  return Optional.ofNullable(code);
	}
}
