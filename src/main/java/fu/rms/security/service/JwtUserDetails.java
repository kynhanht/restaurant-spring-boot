package fu.rms.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fu.rms.entity.Staff;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String code;

	private String phone;

	@JsonIgnore
	private String password;

	private Integer isActivated;

	private Collection<? extends GrantedAuthority> authorities;

	public static JwtUserDetails build(Staff staff) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(staff.getRole().getRoleCode()));
		return new JwtUserDetails(staff.getStaffId(), staff.getStaffCode(), staff.getPhone(), staff.getPassword(),
				staff.getIsActivated(), authorities);
	}

	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return phone;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isActivated == 1 ? true : false;
	}

}
