package com.example.authz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.authz.model.Account;

@Service(value = "userDetailsService")
public class AccountUserDetailsService implements UserDetailsService {

  @Autowired
  AccountSharedService accountSharedService;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      Account account = accountSharedService.findOne(username);
      return new AccountUserDetails(account);
    } catch (IllegalArgumentException e) {
      throw new UsernameNotFoundException("user not found", e);
    }
  }

}
