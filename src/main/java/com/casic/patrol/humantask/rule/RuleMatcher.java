package com.casic.patrol.humantask.rule;

public interface RuleMatcher {
    boolean matches(String text);

    String getValue(String text);
}
