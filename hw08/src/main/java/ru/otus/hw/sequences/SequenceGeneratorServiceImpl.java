package ru.otus.hw.sequences;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.SequenceId;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        FindAndModifyOptions options = new FindAndModifyOptions();
        SequenceId counter = mongoOperations.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                options.returnNew(true).upsert(true),
                SequenceId.class);
        return counter.getSeq();
    }
}
