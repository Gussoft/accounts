package com.gussoft.accounts.core.repository;

import com.gussoft.accounts.core.models.TypeAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TypeAccountRepository extends ReactiveMongoRepository<TypeAccount, String> {

  Mono<TypeAccount> findByName(String name);

}
