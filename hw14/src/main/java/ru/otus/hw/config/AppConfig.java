package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.documents.MongoBook;
import ru.otus.hw.documents.MongoComment;
import ru.otus.hw.entities.Book;
import ru.otus.hw.entities.Comment;
import ru.otus.hw.events.JobExecutionLogListener;
import ru.otus.hw.processors.BookItemProcessor;
import ru.otus.hw.processors.CommentItemProcessor;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.relational.BookRepository;
import ru.otus.hw.repositories.relational.CommentRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    public static final String MIGRATION_JOB_NAME = "migrationJob";

    private static final int CHUNK_SIZE = 2;

    private final BookItemProcessor bookItemProcessor;

    private final CommentItemProcessor commentItemProcessor;

    private final PlatformTransactionManager platformTransactionManager;

    private final JobRepository jobRepository;

    public <T, K> RepositoryItemReader<T> reader(MongoRepository<T, K> repository) {
        var reader = new RepositoryItemReader<T>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        reader.setSort(sort);
        return reader;
    }

    public <T, K> RepositoryItemWriter<T> writer(CrudRepository<T, K> repository) {
        var writer = new RepositoryItemWriter<T>();
        writer.setRepository(repository);
        writer.setMethodName("save");

        return writer;
    }

    @Bean
    public RepositoryItemReader<MongoBook> bookReader(MongoBookRepository mongoBookRepository) {
        return reader(mongoBookRepository);
    }

    @Bean
    public RepositoryItemReader<MongoComment> commentReader(MongoCommentRepository mongoCommentRepository) {
        return reader(mongoCommentRepository);
    }

    @Bean
    public RepositoryItemWriter<Book> bookWriter(BookRepository repository) {
        return writer(repository);
    }

    @Bean
    public RepositoryItemWriter<Comment> commentWriter(CommentRepository repository) {
        return writer(repository);
    }

    @Bean
    public Step booksStep(RepositoryItemReader<MongoBook> bookReader, RepositoryItemWriter<Book> bookWriter) {
        return new StepBuilder("booksStep", jobRepository)
                .<MongoBook, Book>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookReader)
                .processor(bookItemProcessor)
                .writer(bookWriter)
                .startLimit(1)
                .build();
    }

    @Bean
    public Step commentStep(RepositoryItemReader<MongoComment> commentReader,
                            RepositoryItemWriter<Comment> commentWriter) {
        return new StepBuilder("commentStep", jobRepository)
                .<MongoComment, Comment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentReader)
                .processor(commentItemProcessor)
                .writer(commentWriter)
                .startLimit(1)
                .build();
    }


    @Bean
    public Job migrationJob(JobExecutionLogListener listener, Step booksStep, Step commentStep) {
        return new JobBuilder(MIGRATION_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(booksStep)
                .next(commentStep)
                .build();
    }

}
