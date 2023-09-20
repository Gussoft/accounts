package com.gussoft.accounts.core.business;

import com.gussoft.accounts.integration.transfer.request.AccountRequest;
import com.gussoft.accounts.integration.transfer.request.RegistryRequest;
import com.gussoft.accounts.integration.transfer.response.AccountResponse;
import java.math.BigDecimal;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

  Flux<AccountResponse> findAll();

  Mono<AccountResponse> findById(String id);

  Mono<AccountResponse> findByNumberAccount(String number);

  Mono<Map<String,Object>> findByCustomerAccount(String id);

  Mono<AccountResponse> save(Mono<AccountRequest> request);

  Mono<AccountResponse> update(Mono<AccountRequest> request, String id);

  Mono<AccountResponse> addCustomerToAccount(RegistryRequest request, String typeC, String typeA);

  Mono<AccountResponse> updateAmount(String id, BigDecimal amount, String operation);

  Mono<Void> delete(String id);

}
