package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.sequences.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(Long.valueOf(
                    sequenceGenerator.generateSequence(Book.SEQUENCE_NAME)));
        }
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        var source = event.getSource();
        var id = source.getLong("_id");
        commentRepository.deleteAllByBookId(id);
    }
}
