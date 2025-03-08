package com.joshjs.gamangine.validator;

import com.joshjs.gamangine.exception.InvalidInputException;

public class UserInputValidator {

    public static Integer getNewIntValue(Integer currentValue, String calculationType, Integer modificationValue) {
        if (currentValue == null) {
            throw new InvalidInputException("The current value of the attribute trying to be modified isn't set!");
        }
        if (calculationType == null) {
            throw new InvalidInputException("The calculation!");
        }
        if (currentValue == null) {
            throw new InvalidInputException("The current value of the attribute trying to be modified isn't set!");
        }
        return switch (calculationType.toLowerCase()) {
            case "multiplyby" -> currentValue * modificationValue;
            case "minus" -> currentValue - modificationValue;
            case "add" -> currentValue + modificationValue;
            default -> throw new InvalidInputException("Invalid calculation type: " + calculationType);
        };
    }
}
