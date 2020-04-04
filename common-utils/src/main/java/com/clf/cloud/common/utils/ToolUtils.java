package com.clf.cloud.common.utils;

import com.clf.cloud.common.enums.ErrorEnum;
import com.clf.cloud.common.exception.CommonException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : clf
 * @description : 公共工具类
 **/
public class ToolUtils {

  private ToolUtils(){}

  public static boolean strIsNull(String str){
    if(str!=null && str.trim().length()>0){
      return false;
    }
    return true;
  }

  public static boolean strIsNotNul(String str){
    if(str!=null && str.trim().length()>0){
      return true;
    }
    return false;
  }

  // 判断数字正则表达式
  private static final Pattern pattern = Pattern.compile("[0-9]*");

  /**
   * 检查字符串是否是int类型
   * @param str
   * @return
   */
  public static boolean checkInt(String str) {
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 字符串转换为int类型
   * @param str
   * @return
   */
  public static Integer str2Int(String str) {
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return 0;
    } else {
      return Integer.parseInt(str);
    }
  }

  public static Date str2LocalDateTime(String dateStr) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date date= null;
    try {
      date = df.parse(dateStr);
    } catch (ParseException e) {
      throw new CommonException(ErrorEnum.PARSE_ERROR);
    }
    return date;
  }

  public static int randomNum(int high) {
    Random random = new Random();
    return random.nextInt(high);
  }
}