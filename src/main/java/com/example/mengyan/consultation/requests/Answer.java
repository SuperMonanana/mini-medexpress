package com.example.mengyan.consultation.requests;

import com.example.mengyan.consultation.response.Category;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class Answer {
    String questionId;
    String answer;
    Category category;
}
