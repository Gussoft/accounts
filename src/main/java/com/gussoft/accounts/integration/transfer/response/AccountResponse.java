package com.gussoft.accounts.integration.transfer.response;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

  private String id;

  private String customer;
  private String numberAccount;
  private String cci;
  private TypeAccountResponse type;
  private Date createAt;
  private BigDecimal balance;

}
