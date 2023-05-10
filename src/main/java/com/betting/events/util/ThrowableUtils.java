package com.betting.events.util;

import java.util.function.Predicate;

public class ThrowableUtils {
    public static <T> void trueOrElseThrow(Predicate<T> predicate, T obj, Class<? extends RuntimeException> e) {
        boolean test = predicate.test(obj);
        if(!test) {
            try {
                throw e.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static <T> void trueOrElseThrow(Predicate<T> predicate, T obj, RuntimeException e) {
        boolean test = predicate.test(obj);
        if(!test) {
            throw e;
        }
    }
}
