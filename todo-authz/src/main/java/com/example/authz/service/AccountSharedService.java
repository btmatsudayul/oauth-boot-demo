package com.example.authz.service;

import com.example.authz.model.Account;

public interface AccountSharedService {
  Account findOne(String username);
}
