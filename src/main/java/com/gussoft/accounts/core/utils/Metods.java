package com.gussoft.accounts.core.utils;

import java.util.List;

public class Metods<T> {
  private final List<T> list;

  public Metods(List<T> list) {
    this.list = list;
  }

  public T getRegistry(int i) {
    return this.list.get(i);
  }

  public void addRegistry(T p) {
    this.list.add(p);
  }

  public void modify(int i, T p) {
    this.list.set(i, p);
  }

  public void delete(int i) {
      this.list.remove(i);
  }

  public int countRegistry() {
    return this.list.size();
  }

}
