package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private final CsvToBeanReader csvToBeanReader;

    @Override
    public List<Question> findAll() {
        var resourceFileName = fileNameProvider.getTestFileName();

        CsvToBeanReader.Parser<QuestionDto> parser = reader ->
                new CsvToBeanBuilder<QuestionDto>(reader)
                        .withType(QuestionDto.class)
                        .withSeparator(';')
                        .withSkipLines(1)
                        .build().parse();
        try {
            return csvToBeanReader.read(resourceFileName, parser)
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException("Cant read resource file: " + resourceFileName, e);
        }
    }
}
