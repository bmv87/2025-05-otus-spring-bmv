package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private void printAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("%5s%d. %s", " ", i + 1, answers.get(i).text());
        }
    }

    private void printQuestions(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            ioService.printFormattedLine("#%d: %s", i + 1, question.text());
            printAnswers(question.answers());
            ioService.printLine("");
        }
    }

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions;
        try {
            questions = questionDao.findAll();
        } catch (QuestionReadException ex) {
            ioService.printFormattedLine("Error while getting questions: %s", ex.getMessage());
            return;
        }
        printQuestions(questions);
    }
}