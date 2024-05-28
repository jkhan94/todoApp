package com.sparta.todoapp.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponse<T> {
    private int statusCode;
    private String msg;
    private T data;

}