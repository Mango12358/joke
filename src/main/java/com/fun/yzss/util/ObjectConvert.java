package com.fun.yzss.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by fanqq on 2016/9/23.
 */
public class ObjectConvert {

    static Logger logger = LoggerFactory.getLogger(ObjectConvert.class);

    public static Object convert(Object value, Class<?> clazz) {
      if (value == null) {
         return null;
      } else if (value.getClass() == clazz) {
         return value;
      }

      if (clazz == Integer.class || clazz == Integer.TYPE) {
         if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
         } else {
            return Integer.valueOf(value.toString());
         }
      } else if (clazz == Long.class || clazz == Long.TYPE) {
         if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
         } else {
            return Long.valueOf(value.toString());
         }
      } else if (clazz == Double.class || clazz == Double.TYPE) {
         if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
         } else {
            return Double.valueOf(value.toString());
         }
      } else if (clazz == Float.class || clazz == Float.TYPE) {
         if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
         } else {
            return Float.valueOf(value.toString());
         }
      } else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
         String val = value.toString();

         if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("1") || val.equalsIgnoreCase("on")
               || val.equalsIgnoreCase("T") || val.equalsIgnoreCase("Y")) {
            return Boolean.TRUE;
         } else {
            return Boolean.FALSE;
         }
      } else if (clazz == Date.class) {
         if (value instanceof Timestamp) {
            return value;
         }
      } else if (clazz == byte[].class) {
         if (value instanceof Blob) {
            Blob blob = (Blob) value;

            try {
               return blob.getBytes(0L, (int) blob.length());
            } catch (Exception e) {
               throw new RuntimeException("Error when converting Blob to byte[]!", e);
            }
         }
      }
      logger.error("Can't convert type from " + value.getClass() + " to " + clazz );

      return value;
   }
}
