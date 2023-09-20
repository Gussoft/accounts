package com.gussoft.accounts.integration.mappers;

import com.gussoft.accounts.core.models.Account;
import com.gussoft.accounts.core.models.TypeAccount;
import com.gussoft.accounts.integration.transfer.request.AccountRequest;
import com.gussoft.accounts.integration.transfer.response.AccountResponse;
import com.gussoft.accounts.integration.transfer.response.TypeAccountResponse;
import org.springframework.beans.BeanUtils;

public class AccountMapper {

  public AccountMapper() {
  }

  public static Account toAccountRequest(AccountRequest request) {
    Account entity = new Account();
    BeanUtils.copyProperties(request, entity);
    if (request.getType() != null) {
      entity.setType(new TypeAccount(request.getType().getId(), request.getType().getName()));
    }
    return entity;
  }

  public static AccountResponse toAccountResponse(Account entity) {
    AccountResponse response = new AccountResponse();
    BeanUtils.copyProperties(entity, response);
    if (entity.getType() != null) {
      response.setType(new TypeAccountResponse(entity.getType().getId(), entity.getType().getName()));
    }
    return response;
  }

  public static Account toAccountResponse2(AccountResponse response) {
    Account account = new Account();
    BeanUtils.copyProperties(response, account);
    if (response.getType() != null) {
      account.setType(new TypeAccount(response.getType().getId(), response.getType().getName()));
    }
    return account;
  }
}
