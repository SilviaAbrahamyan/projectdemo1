package com.aca.components.util;

/**
 * Created by home on 1/25/2019.
 */
public enum Nullable {
    YES(""),
    NO("NOT NULL");

    private String nullable;

    Nullable(String nullable) {
        this.nullable = nullable;
    }

    public static String getIsNullable(String isNullable) {

        if (Nullable.NO.toString().equals(isNullable)) {
            return Nullable.NO.nullable;
        }
        return Nullable.YES.nullable;

    }

}
