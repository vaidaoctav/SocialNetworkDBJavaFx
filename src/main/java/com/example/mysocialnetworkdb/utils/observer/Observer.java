package com.example.mysocialnetworkdb.utils.observer;

import com.example.mysocialnetworkdb.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
