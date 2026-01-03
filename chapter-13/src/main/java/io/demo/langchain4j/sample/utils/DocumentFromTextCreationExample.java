package io.demo.langchain4j.sample.utils;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

import java.io.File;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocumentFromTextCreationExample {

    Document createDocument(File file) {
        return loadDocument(file.toPath(), new TextDocumentParser());
    }
}
