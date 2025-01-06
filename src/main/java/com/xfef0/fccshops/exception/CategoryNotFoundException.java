package com.xfef0.fccshops.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        super("Category not found!");
    }
}
