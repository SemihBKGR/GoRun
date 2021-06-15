package com.semihbg.gorun.util;

@FunctionalInterface
public interface TextChangeListener {

    void onChanged(Event event,String text);

    enum Event{
        APPEND,
        UPDATE
    }

}
