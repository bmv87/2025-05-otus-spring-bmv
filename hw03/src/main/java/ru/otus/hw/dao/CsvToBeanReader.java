package ru.otus.hw.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface CsvToBeanReader {

    <T> List<T> read(String fileName, Parser<T> parser) throws IOException;

    interface Parser<T> {
        List<T> parse(Reader reader);
    }
}
