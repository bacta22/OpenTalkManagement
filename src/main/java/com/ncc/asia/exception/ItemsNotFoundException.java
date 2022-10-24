package com.ncc.asia.exception;

public class ItemsNotFoundException extends RuntimeException {
    public ItemsNotFoundException(String message) {
        super(message);
    }
}
