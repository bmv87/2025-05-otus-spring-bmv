package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.entities.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBox<T extends ObjectId<O>, O, I> {

    private I inKey;

    private T item;

    public ItemBox(T item) {
        this.item = item;
    }
}
