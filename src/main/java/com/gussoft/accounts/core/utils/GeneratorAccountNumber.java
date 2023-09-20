package com.gussoft.accounts.core.utils;

public class GeneratorAccountNumber {

  private Long valueAct;

  public GeneratorAccountNumber(Long initValue) {
    this.valueAct = initValue;
  }

  public String generate() {
    this.valueAct++;
    String account = "" + this.valueAct;
    return account;
  }


}
