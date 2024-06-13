package com.bonifacio.baon;

import java.util.HashMap;
import java.util.Map;

public class SpinnerImages {
    private static final Map<String, Integer> categoryImageMap = new HashMap<>();

    static {
        categoryImageMap.put("Allowance", R.drawable.allowance);
        categoryImageMap.put("Transport", R.drawable.transport);
        categoryImageMap.put("Miscellaneous", R.drawable.misc);
        categoryImageMap.put("Food", R.drawable.food);

    }

    public static int getImageResource(String category) {
        return categoryImageMap.getOrDefault(category, R.drawable.allowance);
    }
}
