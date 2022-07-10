package io.bodin.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static LinkedHashMap<String, List<String>> copyLinked(Map<String, List<String>> m){
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
        m.entrySet().forEach(e -> result.put(e.getKey(), new ArrayList<>(e.getValue())));
        return result;
    }
}
