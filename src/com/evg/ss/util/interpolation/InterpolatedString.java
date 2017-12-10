package com.evg.ss.util.interpolation;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.InvalidInterpolationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InterpolatedString {

    private final static String pattern = "\\{[^{}]*}";
    private String string;

    public InterpolatedString(String string) {
        this.string = string;
    }

    private static String cutPlaceholders(String string) {
        return string.replace('{', ' ').replace('}', ' ').trim();
    }

    public String calculate() {
        final Pattern pattern = Pattern.compile(InterpolatedString.pattern);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            final String source = cutPlaceholders(matcher.group());
            final SimpleScript ss = SimpleScript.fromSource(source);
            if (!ss.isExpressible())
                throw new InvalidInterpolationException(source);
            string = matcher.replaceFirst(ss.express().eval().asString());
            matcher = pattern.matcher(string);
        }
        return string;
    }
}