package com.joshjs.gamangine.validator;

public class UserInputValidator {

    public static Integer getNewIntValue(Integer currentValue, String calculationType, Integer modificationValue) {
        return switch (calculationType.toLowerCase()) {
            case "multiplyby" -> currentValue * modificationValue;
            case "minus" -> currentValue - modificationValue;
            case "add" -> currentValue + modificationValue;
            default -> throw new IllegalArgumentException("Invalid calculation type: " + calculationType);
        };
    }
}
