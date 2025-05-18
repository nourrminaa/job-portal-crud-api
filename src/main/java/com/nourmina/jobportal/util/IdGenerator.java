package com.nourmina.jobportal.util;

import java.util.UUID;

public class IdGenerator {
    public static String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
