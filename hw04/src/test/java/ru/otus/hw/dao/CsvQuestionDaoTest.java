package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.mockito.BDDMockito.given;

@DisplayName("Csv Question Dao findAll method must")
@SpringBootTest(classes = {CsvQuestionDao.class, CsvToBeanReaderImpl.class})
public class CsvQuestionDaoTest {

    @MockitoBean
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private CsvQuestionDao questionDao;

    @Test
    @DisplayName("call questionDao.findAll method one time and ioService.printFormattedLine with string params one time")
    void shouldReturnNotEmptyQuestionsListWithCorrectResource() {
        given(testFileNameProvider.getTestFileName()).willReturn("questions.csv");
        var list = questionDao.findAll();
        Assertions.assertNotNull(list, "returned value is null");
        Assertions.assertFalse(list.isEmpty(), "returned value is empty");
        Assertions.assertEquals(4, list.size(), "not valid count of questions");
        Assertions.assertInstanceOf(Question.class, list.get(0), "returned value has not valid item type");
    }

    @Test
    @DisplayName(" print error message to output if questionDao.findAll() will throw")
    void shouldThrowQuestionReadExceptionIfNotValidFileName() {
        given(testFileNameProvider.getTestFileName()).willReturn("questions_invalid.csv");
        Assertions.assertThrows(QuestionReadException.class,
                () -> questionDao.findAll(),
                "Expected findAll() to throw, but it didn't");
    }
}
