package com.gussoft.accounts.core.business.impl;

import com.gussoft.accounts.core.business.AccountService;
import com.gussoft.accounts.core.business.CustomerService;
import com.gussoft.accounts.core.models.Account;
import com.gussoft.accounts.core.models.TypeAccount;
import com.gussoft.accounts.core.repository.AccountRepository;
import com.gussoft.accounts.core.utils.AccountNC;
import com.gussoft.accounts.core.utils.CodeCC;
import com.gussoft.accounts.core.utils.GeneratorAccountNumber;
import com.gussoft.accounts.integration.mappers.AccountMapper;
import com.gussoft.accounts.integration.transfer.request.AccountRequest;
import com.gussoft.accounts.integration.transfer.request.RegistryRequest;
import com.gussoft.accounts.integration.transfer.response.AccountResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepository repo;

  @Autowired
  private CustomerService serviceCustomer;

  @Override
  public Flux<AccountResponse> findAll() {
    return repo.findAll().map(AccountMapper::toAccountResponse);
  }

  @Override
  public Mono<AccountResponse> findById(String id) {
    return repo.findById(id).map(AccountMapper::toAccountResponse);
  }

  @Override
  public Mono<AccountResponse> findByNumberAccount(String number) {
    return repo.findByNumberAccount(number).map(AccountMapper::toAccountResponse);
  }

  @Override
  public Mono<Map<String, Object>> findByCustomerAccount(String id) {
    Map<String, Object> map = new HashMap<>();
    return serviceCustomer.findById(id)
      .flatMap(customer -> {
        map.put("Customer", customer);
        return repo.findByCustomer(customer.getId())
          .map(acc -> acc)
          .distinct()
          .collectList()
          .flatMap(accounts -> {
            for (Account ac : accounts) {
              map.put("Account ".concat(ac.getType().getName()), ac);
            }
            return Mono.just(map);
          });
      });
  }

  @Override
  public Mono<AccountResponse> save(Mono<AccountRequest> request) {
    return request.map(AccountMapper::toAccountRequest)
      .flatMap(ac -> {
        GeneratorAccountNumber number = genericAccountNumbers();
        ac.setNumberAccount(number.generate());
        ac.setCci("20-".concat(ac.getNumberAccount().concat("-24")));
        saveAccountNumberCorrelative(ac.getNumberAccount());
        return repo.save(ac);
      })
      .map(AccountMapper::toAccountResponse);
  }

  @Override
  public Mono<AccountResponse> update(Mono<AccountRequest> request, String id) {
    return repo.findById(id)
      .flatMap(a -> request.map(AccountMapper::toAccountRequest)
        .doOnNext(e -> {
          e.setId(id);
          log.info(e.toString());
        }))
      .flatMap(repo::save)
      .map(AccountMapper::toAccountResponse);
  }

  @Override
  public Mono<AccountResponse> addCustomerToAccount(RegistryRequest request, String typeC, String typeA) {

    return validateCustomerWithAccountType(request.getCustomer(), typeA)
      .flatMap(isValid -> {
        if (isValid) {
          return repo.findById(request.getAccount())
            .map(AccountMapper::toAccountResponse)
            .map(AccountMapper::toAccountResponse2)
            .flatMap(account -> {
              if (typeC.equalsIgnoreCase("PERSONAL")) {
                if (account.getCustomer() == null || account.getCustomer().isEmpty()) {
                  account.setCustomer(request.getCustomer());
                  return repo.save(account);
                }
                log.info("la cuenta ya tiene asignada un clienteId ".concat(account.getCustomer()));
              } else if (typeC.equalsIgnoreCase("EMPRESARIAL")) {
                if (account.getType().getName().equalsIgnoreCase("CUENTA CORRIENTE")) {
                  account.setCustomer(request.getCustomer());
                  return repo.save(account);
                }
                log.info("la cuenta empresarial solo puede registrar multiples cuentas corrientes");
              }
              return Mono.just(account);
            }).map(AccountMapper::toAccountResponse);
        } else {
          return repo.findById(request.getAccount())
            .map(AccountMapper::toAccountResponse);
        }
      });
  }

  @Override
  public Mono<AccountResponse> updateAmount(String id, BigDecimal amount, String operation) {
    return repo.findById(id)
      .map(AccountMapper::toAccountResponse)
      .map(AccountMapper::toAccountResponse2)
      .flatMap(account -> {
        if (operation.equalsIgnoreCase("DEPOSITO")) {
          account.setBalance(account.getBalance().add(amount));
        }
        if (operation.equalsIgnoreCase("RETIRO")) {
          account.setBalance(account.getBalance().subtract(amount));
        }
        return repo.save(account);
      }).map(AccountMapper::toAccountResponse);
  }

  @Override
  public Mono<Void> delete(String id) {
    return repo.deleteById(id);
  }

  private GeneratorAccountNumber genericAccountNumbers() {
    AccountNC nc = new AccountNC();
    List<CodeCC> lists = nc.lists();
    Long[] code = new Long[1];
    lists.forEach(c -> code[0] = Long.valueOf(c.getNumber()));
    return new GeneratorAccountNumber(code[0]);
  }

  private void saveAccountNumberCorrelative(String correlative) {
    AccountNC nc = new AccountNC();
    nc.update(new CodeCC(1, correlative));
  }

  private Mono<Boolean> validateCustomerWithAccountType(String id, String types) {
    return serviceCustomer.findById(id)
      .flatMap(customer -> {
        return repo.findByCustomer(customer.getId())
          .map(Account::getType)
          .distinct()
          .collectList()
          .flatMap(type -> {
            if (customer.getType().getName().equalsIgnoreCase("PERSONAL")) {
              List<TypeAccount> account = type.stream()
                .filter(c -> c.getName().equalsIgnoreCase(types))
                .collect(Collectors.toList());

              if (!account.isEmpty()) {
                log.info("Ya cuenta con esta cuenta, Rechazado");
                return Mono.just(false);
              } else {
                log.info("No cuenta con esta cuenta, Aprobado");
                return Mono.just(true);
              }
            }
            log.info("Cliente Empresarial, Solo Cuentas Corrientes");
            return Mono.just(true);
          });
      }).defaultIfEmpty(true);
  }

}