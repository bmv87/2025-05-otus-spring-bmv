package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.AnswerReadException;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        printStartTestMessage();

        var testResult = new TestResult(student);
        List<Question> questions = tryGetQuestions();
        if (questions.isEmpty()) {
            return testResult;
        }

        ask(questions, testResult);

        return testResult;
    }

    private void printAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("%5s%d. %s", " ", i + 1, answers.get(i).text());
        }
    }

    private void printQuestion(Question question) {
        ioService.printFormattedLineLocalized("TestService.question.text", question.text());
        printAnswers(question.answers());
        ioService.printLine("");
    }

    private int printAskNumberOfTrueAnswer(int answersCount) {
        try {
            return ioService.readIntForRangeWithPromptLocalized(
                    1,
                    answersCount,
                    "TestService.print.answer.text",
                    "TestService.wrong.input.value.text");
        } catch (Exception ex) {
            throw new AnswerReadException("TestService.exceeded.attempts.text");
        }
    }

    private void printStartTestMessage() {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
    }

    private List<Question> tryGetQuestions() {
        try {
            return questionDao.findAll();
        } catch (QuestionReadException ex) {
            ioService.printFormattedLineLocalized("TestService.getting.questions.error.text", ex.getMessage());
        }
        return new ArrayList<>(0);
    }

    private void ask(List<Question> questions, TestResult testResult) {
        try {
            for (var question : questions) {
                printQuestion(question);
                var answerNumber = printAskNumberOfTrueAnswer(question.answers().size());
                var isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
                testResult.applyAnswer(question, isAnswerValid);
            }
        } catch (AnswerReadException ex) {
            ioService.printFormattedLineLocalized("TestService.test.pass.error.text", ex.getMessage());
        }
    }

}
