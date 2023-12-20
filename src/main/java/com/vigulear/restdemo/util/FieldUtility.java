package com.vigulear.restdemo.util;

import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : crme059
 * @created : 06-Dec-23, Wednesday
 */
public class FieldUtility {
  public static boolean isFieldValid(Field criteriaField) {
    return criteriaField != null;
  }

  public static List<Field> getAllFields(Class<?> clazz) {
    // Add fields from the current class
    List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

    // Recursively add fields from superclasses
    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null) {
      fields.addAll(getAllFields(superClass));
    }

    return fields;
  }

  public static boolean isFieldCountable(@NotNull Field byField) {
    return byField.getType() == Integer.class
        || byField.getType() == Float.class
        || byField.getType() == Double.class
        || byField.getType() == Short.class
        || byField.getType() == Byte.class
        || byField.getType() == Long.class;
  }

  public static Field getCriteriaFieldForClass(@NotNull String fieldName, Class<?> clazz) {
    List<Field> fields = FieldUtility.getAllFields(clazz);
    return fields.stream()
        .filter(field -> field.getName().equals(fieldName))
        .findFirst()
        .orElse(null);
  }
}
