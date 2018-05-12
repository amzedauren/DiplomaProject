package com.vaadin.starter.beveragebuddy.backend;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.starter.beveragebuddy.ui.encoders.LocalDateToStringEncoder;

public class ReviewService {

  public Map<String, File> nameToFileMap = new HashMap<>();
  private Map<Long, Review> reviews = new HashMap<>();
  private AtomicLong nextId = new AtomicLong(0);
  private final File root = new File(System.getProperty("user.home") + "\\visualizer");

  private ReviewService() {
    if(!root.exists())
      if(!root.mkdir())
        throw new RuntimeException("cant create root folder");
  }

  public static ReviewService getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private String getAttackerIpKey(String str){
    if(str.contains("snort"))
      return "source_ip";
    else if(str.contains("amun"))
      return "attackerIP";
    else if (str.contains("dionaea"))
      return "remote_host";
    else if (str.contains("glastopf"))
      return "source";
    return "";
  }

  private String getPortKey(String str){
    if(str.contains("amun"))
      return "victimPort";
    else if (str.contains("dionaea"))
      return "remote_port";
    else if (str.contains("glastopf"))
      return "source";
    return "";
  }

  public Map<String, Integer> getPortCount(String fileName) {
    Map<String, Integer> map = new HashMap<>();
    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    String key = getPortKey(fileName);
    if(nameToFileMap.containsKey(fileName)) {
      File file = nameToFileMap.get(fileName);

      try(BufferedReader br = new BufferedReader(new FileReader(file))) {
        for(String line; (line = br.readLine()) != null; ) {
          JsonObject object = parser.parse(line).getAsJsonObject();
          object = parser.parse(object.get("payload").getAsString()).getAsJsonObject();
          String port = "";
          if(fileName.contains("glastopf")) {
            port = object.get(key).getAsJsonArray().get(1).getAsString();
          } else {
            if(object.get(key) == null)
              continue;
            port = object.get(key).getAsString();
          }
          if(map.containsKey(port))
            map.put(port, map.get(port) + 1);
          else
            map.put(port, 1);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return map;
  }


  public Map<String, Integer> getCountToIpDate(String fileName) {
    Map<String, Integer> map = new HashMap<>();
    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    String key = getAttackerIpKey(fileName);
    if(nameToFileMap.containsKey(fileName)) {
      File file = nameToFileMap.get(fileName);

      try(BufferedReader br = new BufferedReader(new FileReader(file))) {
        for(String line; (line = br.readLine()) != null; ) {
          JsonObject object = parser.parse(line).getAsJsonObject();
          object = parser.parse(object.get("payload").getAsString()).getAsJsonObject();
          String ip = "";
          if(fileName.contains("glastopf")) {
            ip = object.get(key).getAsJsonArray().get(0).getAsString();
          } else {
            if(object.get(key) == null)
              continue;
            ip = object.get(key).getAsString();
          }

          if(map.containsKey(ip))
            map.put(ip, map.get(ip) + 1);
          else
            map.put(ip, 1);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return map;
  }

  private void getAllFilesRec(List<File> list, File folder){
    File [] files = folder.listFiles();
    if (files != null) {
      for(File file : files) {
        if(file.isDirectory()) {
          getAllFilesRec(list, file);
        } else {
          list.add(file);
        }
      }
    }
  }
  public List<Review> getAll()
  {
    List<Review> list = new ArrayList<>(reviews.values());
    List<File> allFilesInRoot = new ArrayList<>();
    getAllFilesRec(allFilesInRoot, root);
    for(File file: allFilesInRoot) {
      Review review = new Review();
      review.setName(file.getName());
      nameToFileMap.put(file.getName(), file);
      list.add(review);
    }
    return list;
  }

  private static class SingletonHolder {
    static final ReviewService INSTANCE = createDemoReviewService();
    private SingletonHolder() {
    }
    private static ReviewService createDemoReviewService() {
      return new ReviewService();
    }
  }
}
