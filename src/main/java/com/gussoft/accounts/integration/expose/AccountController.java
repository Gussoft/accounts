package com.gussoft.accounts.integration.expose;

import com.gussoft.accounts.core.business.AccountService;
import com.gussoft.accounts.integration.mappers.AccountMapper;
import com.gussoft.accounts.integration.transfer.request.AccountRequest;
import com.gussoft.accounts.integration.transfer.request.RegistryRequest;
import com.gussoft.accounts.integration.transfer.response.AccountResponse;
import com.gussoft.accounts.integration.transfer.response.MessagesResponse;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AccountController {

  @Autowired
  private AccountService service;

  @GetMapping("/accounts")
  public Mono<ResponseEntity<Flux<AccountResponse>>> findAll() {
    return Mono.just(
      ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.findAll()));
  }

  @GetMapping("/accounts/{id}")
  public Mono<ResponseEntity<AccountResponse>> findById(@PathVariable String id) {
    return service.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/accounts/customer/{id}/home")
  public Mono<ResponseEntity<Map<String,Object>>> findByCustomerPanel(@PathVariable String id) {
    return service.findByCustomerAccount(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping(path = "/accounts")
  public Mono<ResponseEntity<AccountResponse>> create(@Valid @RequestBody Mono<AccountRequest> request) {
    return request.flatMap(account -> {
      if (account.getCreateAt() == null) {
        account.setCreateAt(new Date());
      }
      return service.save(Mono.just(account)).map(acc -> ResponseEntity
        .created(URI.create("/api/accounts/".concat(acc.getId()))).body(acc));
    }).onErrorResume(throwable -> {
      return Mono.just(throwable).cast(WebExchangeBindException.class)
        .flatMap(e -> Mono.just(e.getFieldErrors()))
        .flatMapMany(Flux::fromIterable)
        .map(field -> "El campo ".concat(field.getField()).concat(" ").concat(
          Objects.requireNonNull(field.getDefaultMessage())))
        .collectList()
        .flatMap(list -> {
          log.error(list.toString());
          return Mono.just(ResponseEntity.badRequest().build());
        });
    });
  }

  @PutMapping("/accounts/{id}")
  public Mono<ResponseEntity<AccountResponse>> edit(@PathVariable String id,
                                                     @RequestBody Mono<AccountRequest> request) {
    return service.update(request, id)
      .map(ResponseEntity::ok)
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/accounts/customer")
  public Mono<ResponseEntity<AccountResponse>> registryAccountsCustomer(
                                                @RequestParam("typeCustomer") String typeC,
                                                @RequestParam("typeAccount") String typeA,
                                                @RequestBody RegistryRequest request) {

    return service.addCustomerToAccount(request, typeC, typeA)
      .map(response -> {
        log.info(response.getId());
        return ResponseEntity.ok(response);
      })
      .doOnError(Throwable::printStackTrace);
  }

  @DeleteMapping("/accounts/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return service.delete(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
  }

  @PutMapping("/accounts/customer/{id}")
  public Mono<ResponseEntity<AccountResponse>> updateAmount(
    @PathVariable String id, @RequestParam String amount, @RequestParam String operation) {
    return service.updateAmount(id, new BigDecimal(amount), operation)
      .map(ResponseEntity::ok)
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

}
