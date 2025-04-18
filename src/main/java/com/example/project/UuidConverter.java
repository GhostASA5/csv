package com.example.project;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.util.UUID;

public class UuidConverter extends AbstractBeanField<UUID, String> {
    @Override
    protected UUID convert(String value) throws CsvDataTypeMismatchException {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new CsvDataTypeMismatchException(value, UUID.class, "Неверный формат UUID");
        }
    }
}
