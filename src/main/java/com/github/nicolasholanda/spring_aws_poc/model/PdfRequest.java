package com.github.nicolasholanda.spring_aws_poc.model;

import lombok.Data;

@Data
public class PdfRequest {
    private String text;
    private String email;
}