package com.ebanking.IdentityProvider.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ebanking.IdentityProvider.user.Permission.*;


@RequiredArgsConstructor
public enum Role {

  WALLET(Collections.emptySet()),
  BACK_OFFICE(
          Set.of(
                  BACK_OFFICE_READ,
                  BACK_OFFICE_UPDATE,
                  BACK_OFFICE_DELETE,
                  BACK_OFFICE_CREATE
          )
  ),
  AGENT(
          Set.of(
                  BACK_OFFICE_READ,
                  BACK_OFFICE_UPDATE,
                  BACK_OFFICE_DELETE,
                  BACK_OFFICE_CREATE,
                  AGENT_READ,
                  AGENT_UPDATE,
                  AGENT_DELETE,
                  AGENT_CREATE
          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
