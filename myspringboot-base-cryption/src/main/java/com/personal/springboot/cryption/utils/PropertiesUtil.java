package com.personal.springboot.cryption.utils;

import java.util.Properties;

public class PropertiesUtil
{
  private static Properties prop = null;

  public static String getValue(String key)
  {
    return prop.getProperty(key);
  }

  static
  {
    try
    {
      prop = new Properties();
//      prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config/config.properties"));
//      prop.load(PropertiesUtil.class.getResourceAsStream("/config/config.properties"));
      prop.load(ClassLoader.getSystemResourceAsStream("config.properties"));//文件在resources根路径即可
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}