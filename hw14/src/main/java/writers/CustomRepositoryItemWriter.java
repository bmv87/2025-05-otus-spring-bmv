package writers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.otus.hw.cache.CacheHelper;
import ru.otus.hw.entities.ObjectId;
import ru.otus.hw.models.ItemBox;

@RequiredArgsConstructor
public class CustomRepositoryItemWriter<
        T extends ObjectId<Long>,
        O extends ItemBox<T, Long, Long>> implements ItemWriter<O>,
        InitializingBean {

    private final Log logger = LogFactory.getLog(CustomRepositoryItemWriter.class);

    private final CacheHelper cacheHelper;

    private final CrudRepository<T, ?> repository;

    private final String cacheName;

    @Override
    public void write(Chunk<? extends O> chunk) throws Exception {
        if (!CollectionUtils.isEmpty(chunk.getItems())) {
            this.doWrite(chunk);
        }
    }

    protected void doWrite(Chunk<? extends ItemBox<T, Long, Long>> chunk) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Writing to the repository with "
                    + chunk.getItems().size() + " items.");
        }
        for (var item : chunk.getItems()) {
            var result = this.repository.save(item.getItem());
            cacheHelper.putId(cacheName, item.getInKey(), result.getId());
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(this.repository != null,
                "A CrudRepository implementation is required");
        if (this.cacheName != null) {
            Assert.state(StringUtils.hasText(this.cacheName),
                    "cacheName must not be empty.");
        } else {
            logger.debug("cacheName must not be null.");
        }
    }
}
