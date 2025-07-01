package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private InputStream getFileFromResourceAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new QuestionReadException("Resource file not found: " + fileName);
        }
        return inputStream;
    }

    private List<QuestionDto> readFromFile(String fileName) {
        try {
            try (var is = getFileFromResourceAsStream(fileName);
                 InputStreamReader streamReader =
                         new InputStreamReader(is, StandardCharsets.UTF_8)) {

                return new CsvToBeanBuilder<QuestionDto>(streamReader)
                        .withType(QuestionDto.class)
                        .withSeparator(';')
                        .withSkipLines(1)
                        .build().parse();
            }
        } catch (IOException e) {
            throw new QuestionReadException("Cant read resource file: " + fileName, e);
        }
    }

    @Override
    public List<Question> findAll() {
        var resourceFileName = fileNameProvider.getTestFileName();
        return readFromFile(resourceFileName).stream().map(QuestionDto::toDomainObject).toList();
    }
}