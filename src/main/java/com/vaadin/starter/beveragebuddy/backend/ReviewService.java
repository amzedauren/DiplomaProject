package com.vaadin.starter.beveragebuddy.backend;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.starter.beveragebuddy.backend.util.GeoIpUtil;
import java.text.DateFormatSymbols;
public class ReviewService {

    public enum LogType {
    AMUN, SNORT, GLASTOPF
  };

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



  public Map<LogType, Map<Integer, Integer>> getActivityByMonth(){
    Map<LogType, Map<Integer, Integer>> map = new HashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    for(String fName : nameToFileMap.keySet()) {
      LogType type = getType(fName);
      if(type == null)
        continue;
      Map<Integer, Integer> tmp;
      if(!map.containsKey(type)) {
        tmp = new HashMap<>();
        map.put(type, tmp);
      } else
        tmp = map.get(type);

      File file = nameToFileMap.get(fName);
      JsonParser parser = new JsonParser();
      try(BufferedReader br = new BufferedReader(new FileReader(file))) {
        for(String line; (line = br.readLine()) != null; ) {
          JsonObject object = parser.parse(line).getAsJsonObject();
          object = parser.parse(object.get("timestamp").toString()).getAsJsonObject();
          String str = object.get("$date").getAsString();
          Date date = dateFormat.parse(str.replaceAll("T", " "));
          LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
          int month = localDate.getMonthValue();
          if(tmp.containsKey(month)) {
            tmp.put(month, tmp.get(month) + 1);
          } else
            tmp.put(month, 1);

        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return map;
  }

  private LogType getType(String name){
    if(name.contains("snort"))
      return LogType.SNORT;
    else if(name.contains("amun"))
      return LogType.AMUN;
    else if(name.contains("glastopf"))
        return LogType.GLASTOPF;
    else
      return null;
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

  public Map<String, Integer> getCountryCount(String fileName){
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

          String country = GeoIpUtil.getInstance().getCountryName(ip);
          if(map.containsKey(country))
            map.put(country, map.get(country) + 1);
          else
            map.put(country, 1);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return map;
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


  public Map<String, Integer> getCountIp(String fileName) {
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
