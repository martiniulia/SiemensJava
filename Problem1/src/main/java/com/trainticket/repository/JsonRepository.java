package com.trainticket.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonRepository<T> {
    private final File file;
    private final Class<T[]> arrayType;
    private final ObjectMapper mapper;

    public JsonRepository(String filePath, Class<T[]> arrayType) {
        this.file = new File(filePath);
        this.arrayType = arrayType;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public List<T> findAll() {
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try {
            T[] array = mapper.readValue(file, arrayType);
            return new ArrayList<>(Arrays.asList(array));
        } catch (IOException e) {
            System.err.println("Error reading file " + file.getPath() + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAll(List<T> items) {
        try {
            mapper.writeValue(file, items);
        } catch (IOException e) {
            System.err.println("Error writing to file " + file.getPath() + ": " + e.getMessage());
        }
    }
}
