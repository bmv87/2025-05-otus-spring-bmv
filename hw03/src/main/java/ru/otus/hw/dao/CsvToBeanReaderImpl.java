package ru.otus.hw.dao;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvToBeanReaderImpl implements CsvToBeanReader {
    private InputStream getFileFromResourceAsStream(String fileName) throws FileNotFoundException {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileNotFoundException("Resource file not found: " + fileName);
        }
        return inputStream;
    }

    public <T> List<T> read(String fileName, Parser<T> parser) throws IOException {
        try (var is = getFileFromResourceAsStream(fileName);
             InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8)) {

            return parser.parse(streamReader);
        }
    }
}
