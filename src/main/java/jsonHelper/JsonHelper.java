package jsonHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class JsonHelper {

  public static <T> String toJsonString(T target) throws NoSuchFieldException {

    if (target == null) {
      return "null";

    } else if (target instanceof Integer) {
      return target.toString();

    } else if (target instanceof String) {
      StringBuilder str = new StringBuilder();
      str.append("\"");
      str.append(target);
      str.append("\"");
      return str.toString();

    } else if (target.getClass().isArray()) {
      if (target instanceof int[]) {
        StringBuilder backArrayIntToString = new StringBuilder();
        int[] array;
        array = (int[]) target;

        backArrayIntToString.append("[");
        for (int i = 0; i < ((int[]) target).length; i++) {
          backArrayIntToString.append(array[i]);
          backArrayIntToString.append(",");
        }
        backArrayIntToString.deleteCharAt(backArrayIntToString.length() - 1);
        backArrayIntToString.append("]");

        return backArrayIntToString.toString();

      } else if (target instanceof String[]) {
        StringBuilder backArrayString = new StringBuilder();
        String[] array;
        array = (String[]) target;

        backArrayString.append("[");
        for (int i = 0; i < ((String[]) target).length; i++) {
          backArrayString.append("\"");
          backArrayString.append(array[i]);
          backArrayString.append("\"");
          backArrayString.append(",");
        }
        if (backArrayString.length() > 1) {
          backArrayString.deleteCharAt(backArrayString.length() - 1);
        }
        backArrayString.append("]");

        return backArrayString.toString();
      }

      return "[]";

    } else {
      ArrayList<StringBuilder> arrayListFieldValue = new ArrayList<>();

      Class clazz = target.getClass();
      Field[] fields = clazz.getDeclaredFields();
      String keyMapFiled;
      String valueString = "";
      int valueInt = 0;

      for (Field field : fields) {
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        field.setAccessible(true);

        try {
          if (fieldType.getName().equals("java.lang.String")) {
            keyMapFiled = fieldName;
            valueString = (String) field.get(target);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\"");
            stringBuilder.append(keyMapFiled);
            stringBuilder.append("\"");
            stringBuilder.append(":");
            stringBuilder.append(" ");
            stringBuilder.append("\"");
            stringBuilder.append(valueString);
            stringBuilder.append("\"");
            arrayListFieldValue.add(stringBuilder);

          } else {
            keyMapFiled = fieldName;
            valueInt = (int) field.get(target);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\"");
            stringBuilder.append(keyMapFiled);
            stringBuilder.append("\"");
            stringBuilder.append(":");
            stringBuilder.append(" ");
            stringBuilder.append(valueInt);
            arrayListFieldValue.add(stringBuilder);
          }

        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{");
      for (Object obj : arrayListFieldValue) {
        stringBuilder.append(obj);
        stringBuilder.append(",");
        stringBuilder.append(" ");
      }
      stringBuilder.deleteCharAt(stringBuilder.length() - 2);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }

  public static <T> T fromJsonString(String json, Class<T> cls) throws IllegalAccessException,
          InvocationTargetException, InstantiationException, NoSuchFieldException, NoSuchMethodException {

    if (json == null) {
      return null;
    }

    json = json.trim();

    if (json.equals("null") || json.equals("{}") || json.equals("")) {
      return null;
    }

    if (json.contains("{")) {
      json = json.replace("{", "");
      json = json.replace("}", "");
      String[] arr = json.split(",");

      T myClass = null;
      try {
        Class<?> clazz = Class.forName(cls.getName());
        myClass = (T) clazz.newInstance();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      Field[] fields = myClass.getClass().getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        Class fieldType = field.getType();
        String fieldName = field.getName();

        for (String str : arr) {
          String[] pair = str.split(":");
          if (pair[0].trim().equals("\"" + fieldName + "\"")) {
            if (fieldType.toString().equals("class java.lang.String")) {
              field.set(myClass, pair[1].trim().replaceAll("\"", ""));
            } else if (fieldType.toString().equals("int")) {
              field.set(myClass, Integer.parseInt(pair[1].trim()));
            }
          }
        }
      }
      return myClass;
    }

    if (json.equals("[]")) {
      return (T) (Array.newInstance(cls.getComponentType(), 0));
    }

    if (json.contains("\"")) {
      json = json.replace("[", "");
      json = json.replace("]", "");
      json = json.replaceAll("\"", "");
      json = json.trim();
      String[] arr = json.split(",");

      return (T) arr;

    } else {
      json = json.replace("[", "");
      json = json.replace("]", "");
      String[] arr = json.split(",");
      int[] iArr = new int[arr.length];
      for (int i = 0; i < arr.length; i++) {
        iArr[i] = Integer.parseInt(arr[i]);
      }
      return (T) iArr;
    }
  }
}