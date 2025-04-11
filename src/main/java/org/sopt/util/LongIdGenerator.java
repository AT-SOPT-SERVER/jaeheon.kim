package org.sopt.util;

import java.util.concurrent.atomic.AtomicLong;

public class LongIdGenerator implements IdGenerator<Long>{
    private final AtomicLong autoIncrement;

    public LongIdGenerator(Long initialId){
        autoIncrement = new AtomicLong(initialId);
    }

    @Override
    public Long generateId() {
        return autoIncrement.getAndIncrement();
    }
}
