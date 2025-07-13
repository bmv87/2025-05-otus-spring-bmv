package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.CsvToBeanReaderImpl;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Service methods must to")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @Mock
    private IOService ioService;

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
        given(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), eq("Print number of right answer:"), eq("Wrong input value. Try again")))
                .willReturn(1);
        var result = testService.executeTestFor(student);
        assertEquals(2, result.getRightAnswersCount(), "not valid count of Right Answers");
        assertEquals(4, result.getAnsweredQuestions().size(), "not valid count of Answered Questions");
    }

    @Test
    @DisplayName(" print error message to output if ioService.readIntForRangeWithPrompt() will throw")
    void shouldPrintErrorMessageIfHasAnswerReadExceptionAndTestNotPass() {
        given(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), eq("Print number of right answer:"), eq("Wrong input value. Try again")))
                .willThrow(new IllegalArgumentException());
        var result = testService.executeTestFor(student);
        verify(ioService, times(1)).printFormattedLine("Test pass error: %s", "You have exceeded the number of allowed attempts");
        assertEquals(0, result.getRightAnswersCount(), "not valid count of Right Answers");
        assertEquals(0, result.getAnsweredQuestions().size(), "not valid count of Answered Questions");
    }
}

