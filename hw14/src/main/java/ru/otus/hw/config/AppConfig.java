package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.cache.CacheHelper;
import ru.otus.hw.documents.MongoAuthor;
import ru.otus.hw.documents.MongoBook;
import ru.otus.hw.documents.MongoComment;
import ru.otus.hw.documents.MongoGenre;
import ru.otus.hw.entities.Author;
import ru.otus.hw.entities.Book;
import ru.otus.hw.entities.Comment;
import ru.otus.hw.entities.Genre;
import ru.otus.hw.entities.ObjectId;
import ru.otus.hw.events.JobExecutionLogListener;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.ItemBox;
import ru.otus.hw.processors.AuthorItemProcessor;
import ru.otus.hw.processors.BookItemProcessor;
import ru.otus.hw.processors.CommentItemProcessor;
import ru.otus.hw.processors.GenreItemProcessor;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;
import ru.otus.hw.repositories.relational.AuthorRepository;
import ru.otus.hw.repositories.relational.BookRepository;
import ru.otus.hw.repositories.relational.CommentRepository;
import ru.otus.hw.repositories.relational.GenreRepository;
import writers.CustomRepositoryItemWriter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    public static final String MIGRATION_JOB_NAME = "migrationJob";

    private static final int CHUNK_SIZE = 2;

    private final BookItemProcessor bookItemProcessor;

    private final CacheHelper cacheHelper;

    private final AuthorItemProcessor authorItemProcessor;

    private final GenreItemProcessor genreItemProcessor;

    private final CommentItemProcessor commentItemProcessor;

    private final PlatformTransactionManager platformTransactionManager;

    private final JobRepository jobRepository;

    private <T, K> RepositoryItemReader<T> reader(MongoRepository<T, K> repository) {
        var reader = new RepositoryItemReader<T>();
        reader.setRepository(repository);
        reader.setMethodName("findAll");
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);
        reader.setSort(sort);
        return reader;
    }

    private <T, K> RepositoryItemWriter<T> writer(CrudRepository<T, K> repository) {
        var writer = new RepositoryItemWriter<T>();
        writer.setRepository(repository);
        writer.setMethodName("save");

        return writer;
    }

    private <T extends ObjectId<Long>, B extends ItemBox<T, Long, Long>>
    CustomRepositoryItemWriter<T, B> writer(CrudRepository<T, Long> repository, String cacheName) {
        var writer = new CustomRepositoryItemWriter<T, B>(cacheHelper, repository, cacheName);
        return writer;
    }


    private <I, O extends ObjectId<Long>, B extends ItemBox<O, Long, Long>>
    Step buildStep(String name,
                   RepositoryItemReader<I> reader,
                   CustomRepositoryItemWriter<O, B> writer,
                   ItemProcessor<I, B> itemProcessor) {
        return new StepBuilder(name, jobRepository)
                .<I, B>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .faultTolerant()
                .skip(NotFoundException.class)
                .startLimit(1)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoAuthor> authorReader(MongoAuthorRepository repository) {
        return reader(repository);
    }

    @Bean
    public RepositoryItemReader<MongoGenre> genreReader(MongoGenreRepository repository) {
        return reader(repository);
    }

    @Bean
    public RepositoryItemReader<MongoBook> bookReader(MongoBookRepository repository) {
        return reader(repository);
    }

    @Bean
    public RepositoryItemReader<MongoComment> commentReader(MongoCommentRepository repository) {
        return reader(repository);
    }

    @Bean
    public CustomRepositoryItemWriter<Author, ItemBox<Author, Long, Long>> authorWriter(AuthorRepository repository) {
        return writer(repository, CacheHelper.AUTHORS);
    }

    @Bean
    public CustomRepositoryItemWriter<Genre, ItemBox<Genre, Long, Long>> genreWriter(GenreRepository repository) {
        return writer(repository, CacheHelper.GENRES);
    }

    @Bean
    public CustomRepositoryItemWriter<Book, ItemBox<Book, Long, Long>> bookWriter(BookRepository repository) {
        return writer(repository, CacheHelper.BOOKS);
    }

    @Bean
    public RepositoryItemWriter<Comment> commentWriter(CommentRepository repository) {
        return writer(repository);
    }


    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("migrator_batch");
    }


    @Bean
    public Flow authorsFlow(Step authorsStep) {
        return new FlowBuilder<SimpleFlow>("authorsFlow")
                .start(authorsStep)
                .build();
    }

    @Bean
    public Flow genresFlow(Step genresStep) {
        return new FlowBuilder<SimpleFlow>("genresFlow")
                .start(genresStep)
                .build();
    }

    @Bean
    public Step authorsStep(RepositoryItemReader<MongoAuthor> authorReader,
                            CustomRepositoryItemWriter<Author, ItemBox<Author, Long, Long>> authorWriter) {
        return buildStep("authorsStep", authorReader, authorWriter, authorItemProcessor);
    }

    @Bean
    public Step genresStep(RepositoryItemReader<MongoGenre> genreReader,
                           CustomRepositoryItemWriter<Genre, ItemBox<Genre, Long, Long>> genreWriter) {
        return buildStep("genresStep", genreReader, genreWriter, genreItemProcessor);
    }

    @Bean
    public Step booksStep(RepositoryItemReader<MongoBook> bookReader,
                          CustomRepositoryItemWriter<Book, ItemBox<Book, Long, Long>> bookWriter) {
        return buildStep("booksStep", bookReader, bookWriter, bookItemProcessor);
    }

    @Bean
    public Step commentStep(RepositoryItemReader<MongoComment> commentReader,
                            RepositoryItemWriter<Comment> commentWriter) {
        return new StepBuilder("commentStep", jobRepository)
                .<MongoComment, Comment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentReader)
                .processor(commentItemProcessor)
                .writer(commentWriter)
                .faultTolerant()
                .skip(NotFoundException.class)
                .startLimit(1)
                .build();
    }

    @Bean
    public Flow splitFlow(Flow authorsFlow, Flow genresFlow) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(authorsFlow, genresFlow)
                .build();
    }

    @Bean
    public Job migrationJob(JobExecutionLogListener listener,
                            Step booksStep, Step commentStep,
                            Flow splitFlow) {
        return new JobBuilder(MIGRATION_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(splitFlow)
                .next(booksStep)
                .next(commentStep)
                .build()
                .build();
    }

}
