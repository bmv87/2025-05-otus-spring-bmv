package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Service methods must to")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplUnitTest {

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
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

