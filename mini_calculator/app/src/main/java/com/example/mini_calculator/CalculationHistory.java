package com.example.mini_calculator;

public class CalculationHistory {
    private String expression;
    private String result;
    private String time;

    public CalculationHistory(String expression, String result, String time) {
        this.expression = expression;
        this.result = result;
        this.time = time;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public String getTime() {
        return time;
    }
}

