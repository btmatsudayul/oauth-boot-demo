package com.example.authz.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import com.example.authz.model.Account;

public class AccountUserDetails extends User {

  private static final long serialVersionUID = 1L;

  private final Account account;

  public AccountUserDetails(Account account) {
    super(account.getUsername(), account.getPassword(),
        AuthorityUtils.createAuthorityList("ROLE_USER"));
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }
}
