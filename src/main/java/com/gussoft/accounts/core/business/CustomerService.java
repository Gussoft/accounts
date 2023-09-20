package com.gussoft.accounts.core.business;

import com.gussoft.accounts.core.models.dto.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {

  Mono<Customer> findById(String id);

}
