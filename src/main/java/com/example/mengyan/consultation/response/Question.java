package com.example.mengyan.consultation.response;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Question {
    @NonNull
    String id;

    @NonNull
    String questionText;

    @NonNull
    Category category;

    List<String> options; // Options for the question (e.g., "Yes", "No")
}
