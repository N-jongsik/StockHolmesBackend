package com.example.wms.notification.adapter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomSseEmitter extends SseEmitter {
    private final AtomicBoolean disposed = new AtomicBoolean(false);

    public CustomSseEmitter(Long timeout) {
        super(timeout);
        this.onCompletion(() -> disposed.set(true));
        this.onTimeout(() -> disposed.set(true));
        this.onError((ex) -> disposed.set(true));
    }

    public boolean isDisposed() {
        return disposed.get();
    }
}
