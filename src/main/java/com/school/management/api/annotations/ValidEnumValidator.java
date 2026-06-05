
package com.school.management.api.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidEnumValidator
    implements ConstraintValidator<ValidEnum, String> {

  private String[] acceptedValues;

  @Override
  public void initialize(ValidEnum annotation) {
    acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
        .map(Enum::name).toArray(String[]::new);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return true;
    }

    return Arrays.asList(acceptedValues).contains(value);
  }
}