package com.example.springboot.validate;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for validate annotations
 */
public final class ValidateUtils {
    private ValidateUtils(){};

    /**
     * Check all items in the list is all true
     *
     * @param list list of boolean value
     * @return true or false
     */
    public static boolean isAllTrue(List<Boolean> list){
        return list.stream().allMatch(item -> item);
    }

    /**
     * Check any items in the list is true
     *
     * @param list list of boolean value
     * @return true or false
     */
    public static boolean isAnyTrue(List<Boolean> list){
        return list.stream().anyMatch(item -> item);
    }
    public static boolean isOnlyOneTrue(List<Boolean> list){
        Predicate<Boolean> byTrue = value -> value;
        return list.stream().filter(byTrue).count() == 1;
    }
}
