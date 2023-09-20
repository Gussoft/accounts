package com.gussoft.accounts.core.repository;

import com.gussoft.accounts.core.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

  Mono<Account> findByNumberAccount(String number);

  Flux<Account> findByCustomer(String id);

}
