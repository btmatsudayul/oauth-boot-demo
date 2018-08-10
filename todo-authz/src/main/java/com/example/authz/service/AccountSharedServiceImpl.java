package com.example.authz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.authz.mapper.AccountMapper;
import com.example.authz.model.Account;

@Service
public class AccountSharedServiceImpl implements AccountSharedService {

  @Autowired
  AccountMapper accountMapper;

  @Transactional(readOnly=true)
  @Override
  public Account findOne(String username) {
      Account account = accountMapper.findOne(username);
      if (account == null) {
        throw new IllegalArgumentException(username + " is invalid");
      }
      return account;
  }

}
