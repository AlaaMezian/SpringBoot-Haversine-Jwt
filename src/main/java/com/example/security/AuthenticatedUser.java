package com.example.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.Role;
import com.example.entity.User;

/*
 * created by Ala'a Mezian
 */

public class AuthenticatedUser implements UserDetails {

	private static final long serialVersionUID = 2245251454156719626L;

	private Long id;
	private String username;
	private String password;
	private String email;
	private Long refrencedId;
	private Collection<? extends GrantedAuthority> authorities;

	public AuthenticatedUser(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.setEmail(email);
		this.authorities = authorities;

	}

	public static AuthenticatedUser create(User user) {
		List<Role> roles = new ArrayList<>();
		roles.add(user.getRole());
		List<GrantedAuthority> authorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		return new AuthenticatedUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
				authorities);
	}

	public void setRefrencedId(long refrencedId) {
		this.refrencedId = refrencedId;
	}

	public Long getId() {
		return id;
	}

	public Long getRefrencedId() {
		return refrencedId;
	}

	public void setRefrencedId(Long refrencedId) {
		this.refrencedId = refrencedId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AuthenticatedUser that = (AuthenticatedUser) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
