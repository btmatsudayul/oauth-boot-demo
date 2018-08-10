package com.example.authz.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.authz.model.Account;

@Mapper
public interface AccountMapper {

  Account findOne(String username);
}
