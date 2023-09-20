package com.gussoft.accounts.core.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountNC {

  private Metods<CodeCC> metods;
  private final List<CodeCC> list;
  private final String path = "AccountNC.txt";

  private CodeCC codeCC;

  public AccountNC() {
    this.list = new ArrayList<>();
    metods = new Metods<>(list);
    loadList();
  }

  private void loadList() {
    CodeCC number = null;
    for (String data : AccessFile.loadFiles(path)) {
      StringTokenizer st = new StringTokenizer(data, ",");
      number = new CodeCC(Integer.parseInt(st.nextToken()), st.nextToken());
      metods.addRegistry(number);
    }
  }

  public List<CodeCC> lists() {
    List<CodeCC> numbers = new ArrayList<>();
    try {
      CodeCC number = null;
      for (String data : AccessFile.loadFiles(path)) {
        StringTokenizer st = new StringTokenizer(data, ",");
        number = new CodeCC(Integer.parseInt(st.nextToken()), st.nextToken());
        numbers.add(number);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return numbers;
  }

  public void save(CodeCC obj) {
    PrintWriter pw;
    FileWriter fw;
    try {
      fw = new FileWriter("Files/" + path);
      pw = new PrintWriter(fw);
      metods.addRegistry(obj);
      for (int i = 0; i < metods.countRegistry(); i++) {
        codeCC = metods.getRegistry(i);
        pw.println(String.valueOf(codeCC.getId()).concat(",").concat(codeCC.getNumber()));
      }
      pw.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void update(CodeCC obj) {
    PrintWriter pw;
    FileWriter fw;
    try {
      fw = new FileWriter("Files/" + path);
      pw = new PrintWriter(fw);
      int code = findCode(obj.getId());
      if (code == -1) {
        metods.addRegistry(obj);
      } else {
        metods.modify(code, obj);
      }
      for (int i = 0; i < metods.countRegistry(); i++) {
        codeCC = metods.getRegistry(i);
        pw.println(String.valueOf(codeCC.getId()).concat(",").concat(codeCC.getNumber()));
      }
      pw.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public int findCode(int code) {
    for (int i = 0; i < metods.countRegistry(); i++) {
      if (code ==  metods.getRegistry(i).getId()) {
        return i;
      }
    }
    return -1;
  }
}
