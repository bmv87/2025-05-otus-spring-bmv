package ru.otus.hw.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class MongoBook {

    @Id
    @Field(name = "id")
    private Long id;

    @Field(name = "title")
    private String title;

    private MongoAuthor author;

    private List<MongoGenre> genres;

    public MongoBook(String title, MongoAuthor author, List<MongoGenre> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
