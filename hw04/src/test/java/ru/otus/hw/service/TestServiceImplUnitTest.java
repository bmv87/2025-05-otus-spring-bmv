package ru.otus.hw.service;

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
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Service methods must to")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestServiceImplUnitTest {

    @ComponentScan(
            basePackages = "ru.otus.hw.service",
            includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {TestService.class})
    )
    @Configuration
    static class NestedConfiguration {
    }

    @MockitoBean
    private LocalizedIOService ioService;

    @MockitoBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    @Test
    @DisplayName("call questionDao.findAll method one time and ioService.printFormattedLineLocalized with string params one time")
    void shouldExecuteServiceMethodsForStartTesting() {
        var student = new Student("Test", "Testov");
        testService.executeTestFor(student);
        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).printLineLocalized("TestService.answer.the.questions");
    }

    @Test
    @DisplayName(" print error message to output if questionDao.findAll() will throw")
    void shouldPrintErrorMessageIfHasQuestionReadException() {
        var student = new Student("Test", "Testov");
        given(questionDao.findAll()).willThrow(new QuestionReadException("File not found"));
        testService.executeTestFor(student);
        verify(ioService, times(1)).printFormattedLineLocalized("TestService.getting.questions.error.text", "File not found");
    }

}

