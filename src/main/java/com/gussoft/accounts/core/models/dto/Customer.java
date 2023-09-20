package com.gussoft.accounts.core.models.dto;

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
public class Customer {

  private String id;

  private String name;
  private String lastName;
  private String lastName2;
  private String address;
  private String phone;
  private Date createAt;
  private TypeCustomer type;
  private String document;

}
