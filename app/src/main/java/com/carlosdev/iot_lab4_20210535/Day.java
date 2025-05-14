package com.carlosdev.iot_lab4_20210535;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Day {
    public double maxtemp_c;
    public double mintemp_c;
    public String conditionText;

    @SerializedName("condition")
    public void unpackCondition(Map<String, Object> condition) {
        conditionText = (String) condition.get("text");
    }
}

