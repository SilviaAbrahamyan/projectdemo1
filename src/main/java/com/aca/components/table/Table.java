package com.aca.components.table;

import java.util.List;

public interface Table {
    boolean isEnabled();

    List getColumns();
    String getName();
}