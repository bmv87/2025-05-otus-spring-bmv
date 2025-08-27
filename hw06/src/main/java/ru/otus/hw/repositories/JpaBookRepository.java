package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;


@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("books-authors-entity-graph");
        TypedQuery<Book> query = em.createQuery("select distinct b from " +
                "from Book b " +
                "left join fetch b.genres " +
                "where b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint(FETCH.getKey(), entityGraph);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("books-authors-entity-graph");
        TypedQuery<Book> query = em.createQuery(
                "select distinct b " +
                        "from Book b " +
                        "left join fetch b.genres", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery(
                "delete from Book b where b.id = :id"
        );

        query.setParameter("id", id);
        query.executeUpdate();
    }
}
