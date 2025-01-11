package com.team5430.util;

import java.util.function.BooleanSupplier;

public class TernaryVoid {


    /**
     * Constructs a {@code TernaryVoid} instance and immediately executes
     * one of the provided {@code Runnable} actions based on the given condition.
     *
     * @param condition The condition to evaluate, provided as a {@code BooleanSupplier}.
     * @param ifTrue The action to run if the condition evaluates to true, provided as a {@code Runnable}.
     * @param ifFalse The action to run if the condition evaluates to false, provided as a {@code Runnable}.
     */
    public TernaryVoid(BooleanSupplier condition, Runnable ifTrue, Runnable ifFalse){
        (condition.getAsBoolean() ? ifTrue : ifFalse ).run();
    }
}