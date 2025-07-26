package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.mockito.BDDMockito.given;

@DisplayName("Csv Question Dao findAll method must")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CsvQuestionDaoTest {

    @ComponentScan(
            basePackages = "ru.otus.hw.dao",
            includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CsvQuestionDao.class})
    )
    @Configuration
    static class NestedConfiguration {
    }

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
