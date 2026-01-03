package io.demo.langchain4j.sample.service;

import io.quarkus.runtime.annotations.RegisterForReflection;


@RegisterForReflection
public record Comment(String comment) {}
