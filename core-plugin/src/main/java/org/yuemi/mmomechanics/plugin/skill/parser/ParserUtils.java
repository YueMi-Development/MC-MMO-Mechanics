package org.yuemi.mmomechanics.plugin.skill.parser;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ParserUtils {

    public static @NotNull Map<String, String> parseOptions(@NotNull String clean) {
        Map<String, String> map = new HashMap<>();
        if (!clean.contains("{") || !clean.contains("}")) {
            return map;
        }
        String optionsStr = clean.substring(clean.indexOf("{") + 1, clean.lastIndexOf("}"));
        
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        char quoteChar = 0;
        
        for (int i = 0; i < optionsStr.length(); i++) {
            char c = optionsStr.charAt(i);
            if ((c == '"' || c == '\'') && (i == 0 || optionsStr.charAt(i - 1) != '\\')) {
                if (inQuotes) {
                    if (c == quoteChar) {
                        inQuotes = false;
                    } else {
                        current.append(c);
                    }
                } else {
                    inQuotes = true;
                    quoteChar = c;
                }
            } else if ((c == ',' || c == ';') && !inQuotes) {
                parsePair(current.toString(), map);
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            parsePair(current.toString(), map);
        }
        return map;
    }
    
    private static void parsePair(String pair, Map<String, String> map) {
        int eqIndex = pair.indexOf('=');
        if (eqIndex != -1) {
            String key = pair.substring(0, eqIndex).trim().toLowerCase();
            String val = pair.substring(eqIndex + 1).trim();
            if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
                val = val.substring(1, val.length() - 1);
            } else if (val.startsWith("'") && val.endsWith("'") && val.length() >= 2) {
                val = val.substring(1, val.length() - 1);
            }
            map.put(key, val);
        }
    }
}
