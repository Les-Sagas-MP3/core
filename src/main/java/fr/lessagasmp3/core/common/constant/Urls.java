package fr.lessagasmp3.core.common.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Urls {

    public static final String PROTOCOL = "protocol";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String HOST = "host";

    public static Map<String, String> splitUrl(String url) {
        String pattern = "^(\\w+?://)(\\w+?):(.+?)@(.+?)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        Map<String, String> split = new HashMap<>();
        if (m.find( )) {
            split.put(PROTOCOL, m.group(1));
            split.put(USERNAME, m.group(2));
            split.put(PASSWORD, m.group(3));
            split.put(HOST, m.group(4));
        } else {
            throw new IllegalArgumentException("No match");
        }
        return split;
    }
}
