package com.it.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ErrorDto {

    private String msg;
    private LocalDate time;
    private String uri;

    public ErrorDto(String msg, LocalDate time, String uri) {
        this.msg = msg;
        this.time = time;
        this.uri = uri;
    }

    public ErrorDto() {
    }
}
