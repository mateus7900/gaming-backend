package com.gaming.utils;

import java.util.Random;
import java.util.UUID;

public class IdGeneratorUtil {

    public static String getNumericString(){
        return UUID.randomUUID().toString();
    }
}
