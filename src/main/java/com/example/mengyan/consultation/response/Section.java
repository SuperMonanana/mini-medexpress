package com.example.mengyan.consultation.response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
public class Section {
    @NonNull
    String title; // E.g., "About You", "Symptoms"

    @NonNull
    List<Question> questions; // Questions belonging to the section
}
