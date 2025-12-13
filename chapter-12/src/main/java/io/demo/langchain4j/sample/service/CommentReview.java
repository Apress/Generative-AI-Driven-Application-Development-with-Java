package io.demo.langchain4j.sample.service;

import com.fasterxml.jackson.annotation.JsonCreator;

public record CommentReview(Rating rating, String message) {
    @JsonCreator
    public CommentReview {}
}
