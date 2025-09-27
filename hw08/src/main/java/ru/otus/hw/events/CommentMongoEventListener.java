package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.sequences.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class CommentMongoEventListener extends AbstractMongoEventListener<Comment> {

    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Comment> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(Long.valueOf(
                    sequenceGenerator.generateSequence(Comment.SEQUENCE_NAME)));
        }
    }
}
