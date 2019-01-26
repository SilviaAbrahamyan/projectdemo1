package com.aca.converter;

import com.aca.components.Schema;

public interface Converter<T, R> {
    Schema<R> convert(Schema<T> schema);

}
