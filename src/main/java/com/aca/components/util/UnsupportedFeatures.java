package com.aca.components.util;

import java.util.ArrayList;
import java.util.List;

public class UnsupportedFeatures {
    private static List<String> unsupportedFeatures = new ArrayList<>();

    public static List<String> getUnsupportedFeatures() {
        return unsupportedFeatures;
    }

    public static void add(String s) {
        unsupportedFeatures.add(s);
    }


}
