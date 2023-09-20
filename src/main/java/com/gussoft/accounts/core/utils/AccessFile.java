package com.gussoft.accounts.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class AccessFile {

  public static List<String> loadFiles(String path) {
    List<String> list = null;
    FileReader fileReader;
    BufferedReader bu;
    String line;
    File carp = new File("Files");
    File file = new File("Files/" + path);
    try {
      if (!carp.exists()) {
        FileUtils.forceMkdir(carp);
      }
      if (!file.exists()) {
        file.createNewFile();
      }
      list = new ArrayList<>();
      fileReader = new FileReader("Files/" + path);
      bu = new BufferedReader(fileReader);
      while ((line = bu.readLine()) != null) {
        list.add(line);
      }
      bu.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    return list;
  }
}
