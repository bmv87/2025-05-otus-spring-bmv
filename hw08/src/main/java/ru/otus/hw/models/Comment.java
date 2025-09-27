package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Transient
    public static final String SEQUENCE_NAME = "comments_sequence";

    @Id
    @Field(name = "id")
    private Long id;

    @Field(name = "content")
    private String content;

    @DBRef(lazy = true)
    private Book book;

    public Comment(String content, Book book) {
        this.content = content;
        this.book = book;
    }
}
