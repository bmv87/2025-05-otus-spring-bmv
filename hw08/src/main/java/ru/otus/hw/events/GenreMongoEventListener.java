package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.sequences.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Genre> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(Long.valueOf(
                    sequenceGenerator.generateSequence(Genre.SEQUENCE_NAME)));
        }
    }
}
