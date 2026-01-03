package io.demo.langchain4j.sample.utils;

import dev.langchain4j.data.document.Document;
import io.demo.langchain4j.sample.dto.Customer;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ApplicationStartup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);

    @Inject
    IngestorExample ingestorExample;

    @Inject
    DocumentFromTextCreationExample documentFromTextCreationExample;

    @Transactional
    void onStartup(@Observes StartupEvent event) {
        LOGGER.info("Application starting...");

        // 1. Insert a customer into the database
        insertInitialCustomer();

        // 2. Read documents from catalog.csv and ingest into Redis
        try {
            List<Document> documents = readDocumentsFromCatalogCsv();
            ingestDocumentsIntoRedis(documents);
        } catch (IOException e) {
            LOGGER.error("Error processing catalog.csv: {}", e.getMessage(), e);
        }
    }

    private void insertInitialCustomer() {
        LOGGER.info("Inserting initial customer...");
        Customer customer = new Customer("John Doe", "123 Main St");
        customer.persist();
        LOGGER.info("Initial customer inserted with ID: {}", customer.id);
    }

    private List<Document> readDocumentsFromCatalogCsv() throws IOException {
        // Load the file from the resources folder.
        URL resource = getClass().getClassLoader().getResource("catalog.csv");
        if (resource == null) {
            throw new IOException("File catalog.csv not found in resources folder");
        }
        File file = new File(resource.getFile());
        Document document = documentFromTextCreationExample.createDocument(file);

        List<Document> documents = new ArrayList<>();
        documents.add(document);
        LOGGER.info("Documents read from catalog.csv");
        return documents;
    }

    private void ingestDocumentsIntoRedis(List<Document> documents) {
        LOGGER.info("Ingesting documents into Redis...");
        ingestorExample.ingest(documents);
        LOGGER.info("Documents ingested into Redis.");
    }
}

