package com.gussoft.accounts.core.business.impl;

import com.gussoft.accounts.core.business.TypeAccountService;
import com.gussoft.accounts.core.repository.TypeAccountRepository;
import com.gussoft.accounts.integration.mappers.TypeAccountMapper;
import com.gussoft.accounts.integration.transfer.request.TypeAccountRequest;
import com.gussoft.accounts.integration.transfer.response.TypeAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TypeAccountServiceImpl implements TypeAccountService {

  @Autowired
  private TypeAccountRepository repo;

  @Override
  public Flux<TypeAccountResponse> findAllTypeCustomer() {
    return repo.findAll().map(TypeAccountMapper::toTypeAccountResponse);
  }

  @Override
  public Mono<TypeAccountResponse> findById(String id) {
    return repo.findById(id).map(TypeAccountMapper::toTypeAccountResponse);
  }

  @Override
  public Mono<TypeAccountResponse> findByName(String name) {
    return repo.findByName(name).map(TypeAccountMapper::toTypeAccountResponse);
  }

  @Override
  public Mono<TypeAccountResponse> save(Mono<TypeAccountRequest> request) {
    return request.map(TypeAccountMapper::toTypeAccountRequest)
      .flatMap(repo::save)
      .map(TypeAccountMapper::toTypeAccountResponse);
  }

  @Override
  public Mono<Void> delete(String id) {
    return repo.deleteById(id);
  }
}
