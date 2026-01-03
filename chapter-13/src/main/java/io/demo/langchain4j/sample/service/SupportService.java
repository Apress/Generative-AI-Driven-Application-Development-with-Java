package io.demo.langchain4j.sample.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.demo.langchain4j.sample.dto.CustomerRepository;
import io.demo.langchain4j.sample.rag.RetrievalAugmentorExample;
import io.demo.langchain4j.sample.utils.ChatMemoryBean;
import io.quarkiverse.langchain4j.RegisterAiService;


@RegisterAiService(
    tools = {CustomerRepository.class},
    chatMemoryProviderSupplier = ChatMemoryBean.class,
    retrievalAugmentor = RetrievalAugmentorExample.class
)
public interface SupportService {
    @SystemMessage("""
        You are working for an ecommerce company, processing comments about
        their products and providing additional information as requested
       	by the customer. Categorize comments into positive, neutral and negative ones,
	    responding with a JSON format document that includes a
        personalized greeting if you have the customer's details.
    """)
    @UserMessage("""
            Your task is to process the comment delimited by ---.
            Apply sentiment analysis to the comment to determine
            if it is positive, neutral or negative, considering various languages.
    
            For example:
            - `I love the user experience in your app, you are my best place to be!` is a 'POSITIVE' comment
            - `J'adore ton application` is a 'POSITIVE' comment
            - `I don't want to use your app, it's too slow!` is a 'NEGATIVE' comment
    
            Respond with a JSON format document containing:
            - the 'rating' key set to 'POSITIVE' if the comment is
            positive, 'NEUTRAL' if the comment is neutral, 'NEGATIVE' otherwise
            - the 'message' key set to a message thanking or apologizing
            to the customer. These messages must be polite and match the
            comment's language.
            - Find the customer name if there is an id provided
            and use it to greet the customer. Also provide any additional
            information as requested by the customer.
    
            ---
            {comment}
            {customerId}
            ---
    """)
    SupportComent categorize(String comment, long customerId);
}

