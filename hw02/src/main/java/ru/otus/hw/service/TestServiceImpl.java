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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private void printAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("%5s%d. %s", " ", i + 1, answers.get(i).text());
        }
    }

    private void printQuestion(Question question) {
        ioService.printFormattedLine("Question: %s", question.text());
        printAnswers(question.answers());
        ioService.printLine("");
    }

    private int printAskNumberOfTrueAnswer(int answersCount) {
        try {
            return ioService.readIntForRangeWithPrompt(
                    1,
                    answersCount,
                    "Print number of right answer:",
                    "Wrong input value. Try again");
        } catch (Exception ex) {
            throw new AnswerReadException("You have exceeded the number of allowed attempts");
        }
    }

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var testResult = new TestResult(student);
        List<Question> questions;
        try {
            questions = questionDao.findAll();
        } catch (QuestionReadException ex) {
            ioService.printFormattedLine("Error while getting questions: %s", ex.getMessage());
            return testResult;
        }

        try {
            for (var question : questions) {
                var isAnswerValid = false;
                printQuestion(question);
                var answerNumber = printAskNumberOfTrueAnswer(question.answers().size());
                isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
                testResult.applyAnswer(question, isAnswerValid);
            }
        } catch (AnswerReadException ex) {
            ioService.printFormattedLine("Test pass error: %s", ex.getMessage());
        }
        return testResult;
    }
}
