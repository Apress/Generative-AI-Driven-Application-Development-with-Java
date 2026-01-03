package io.demo.langchain4j.sample.controller;

import io.demo.langchain4j.sample.service.Comment;
import io.demo.langchain4j.sample.service.SupportService;
import io.demo.langchain4j.sample.service.CommentReview;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/comment")
public class CommentResource {

    @Inject
    SupportService categorize;

    @POST
    public CommentReview categorize(Comment comment) {
        return categorize.categorize(comment.comment(), 1);
    }
}
