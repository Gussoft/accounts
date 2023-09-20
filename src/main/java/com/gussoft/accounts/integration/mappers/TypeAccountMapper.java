package com.gussoft.accounts.integration.mappers;


import com.gussoft.accounts.core.models.TypeAccount;
import com.gussoft.accounts.integration.transfer.request.TypeAccountRequest;
import com.gussoft.accounts.integration.transfer.response.TypeAccountResponse;
import org.springframework.beans.BeanUtils;

public class TypeAccountMapper {

  public TypeAccountMapper() {
  }

  public static TypeAccount toTypeAccountRequest(TypeAccountRequest request) {
    TypeAccount entity = new TypeAccount();
    BeanUtils.copyProperties(request, entity);
    return entity;
  }

  public static TypeAccountResponse toTypeAccountResponse(TypeAccount customer) {
    TypeAccountResponse response = new TypeAccountResponse();
    BeanUtils.copyProperties(customer, response);
    return response;
  }

}
