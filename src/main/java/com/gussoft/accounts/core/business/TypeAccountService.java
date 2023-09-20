package com.gussoft.accounts.core.business;

import com.gussoft.accounts.integration.transfer.request.TypeAccountRequest;
import com.gussoft.accounts.integration.transfer.response.TypeAccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeAccountService {

  Flux<TypeAccountResponse> findAllTypeCustomer();

  Mono<TypeAccountResponse> findById(String id);

  Mono<TypeAccountResponse> findByName(String name);

  Mono<TypeAccountResponse> save(Mono<TypeAccountRequest> request);

  Mono<Void> delete(String id);

}
