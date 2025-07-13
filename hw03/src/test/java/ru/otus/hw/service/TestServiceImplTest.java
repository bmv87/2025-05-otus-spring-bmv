package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.CsvToBeanReaderImpl;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Service methods must to")
@ExtendWith(SpringExtension.class)
public class TestServiceImplTest {

    @MockitoBean
    private TestFileNameProvider testFileNameProvider;

    @MockitoBean
    private LocalizedIOService ioService;

    private Student student;

    private TestServiceImpl testService;

    @BeforeEach
    public void beforeTest() {
        student = new Student("Test", "Testov");
        var questionDao = new CsvQuestionDao(testFileNameProvider, new CsvToBeanReaderImpl());
        given(testFileNameProvider.getTestFileName()).willReturn("questions.csv");
        testService = new TestServiceImpl(ioService, questionDao);
    }


    @Test
    @DisplayName(" print error message to output if ioService.readIntForRangeWithPrompt() will throw")
    void shouldHasAnsweredQuestions() {
        given(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), eq("TestService.print.answer.text"), eq("TestService.wrong.input.value.text")))
                .willReturn(1);
        var result = testService.executeTestFor(student);
        assertEquals(2, result.getRightAnswersCount(), "not valid count of Right Answers");
        assertEquals(4, result.getAnsweredQuestions().size(), "not valid count of Answered Questions");
    }

    @Test
    @DisplayName(" print error message to output if ioService.readIntForRangeWithPrompt() will throw")
    void shouldPrintErrorMessageIfHasAnswerReadExceptionAndTestNotPass() {
        given(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), eq("TestService.print.answer.text"), eq("TestService.wrong.input.value.text")))
                .willThrow(new IllegalArgumentException());
        var result = testService.executeTestFor(student);
        verify(ioService, times(1)).printFormattedLineLocalized("TestService.test.pass.error.text", "TestService.exceeded.attempts.text");
        assertEquals(0, result.getRightAnswersCount(), "not valid count of Right Answers");
        assertEquals(0, result.getAnsweredQuestions().size(), "not valid count of Answered Questions");
    }
}

