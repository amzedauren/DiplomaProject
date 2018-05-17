package com.vaadin.starter.beveragebuddy.backend.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CountryResponse;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GeoIpUtil {
  private File database = new File("C:\\Users\\Amze\\Documents\\IPDB\\GeoLite2-Country_20180501\\GeoLite2-Country.mmdb");
  private DatabaseReader reader = new DatabaseReader.Builder(database).build();
  private static GeoIpUtil instance = null;
  private GeoIpUtil() throws IOException {}

  public static GeoIpUtil getInstance(){
    if(instance != null)
      return instance;
    try {
      instance = new GeoIpUtil();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return instance;
  }
  public String getCountryName(String ip){
    if(InetAddressValidator.getInstance().isValidInet4Address(ip)) {
      try {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse country = reader.country(ipAddress);
        return country.getRegisteredCountry().getName();
      } catch (UnknownHostException | AddressNotFoundException e) {
        return "Unknown Host";
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return "Invalid Ip";
  }
}
