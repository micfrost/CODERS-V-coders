package com.dci.a3m.service;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Marks this class as a Spring service component
@Service
public class BadWordsFilterService {

    // Logger instance for logging events
    private static final Logger logger = LoggerFactory.getLogger(BadWordsFilterService.class);

    // Injects the value of the "obscene.words" property from the application properties
    @Value("${obscene.words}")
    private String obsceneWordsProperty;

    // List to store obscene words
    private List<String> obsceneWords;
    // List to store compiled regex patterns for obscene words
    private List<Pattern> obscenePatterns;

    // Method annotated with @PostConstruct to run once the bean is fully initialized
    @PostConstruct
    public void init() {
        // Split the obscene words property into a list of words
        obsceneWords = Arrays.asList(obsceneWordsProperty.split(","));
        // Compile regex patterns for each obscene word, ignoring case
        obscenePatterns = obsceneWords.stream()
                .map(word -> Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE))
                .collect(Collectors.toList());
    }

    // Returns the list of obscene words
    public List<String> getObsceneWords() {
        return obsceneWords;
    }

    // Checks if the given text contains any obscene language
    public boolean containsObsceneLanguage(String text) {
        logger.info("Checking for obscene language in text");
        // Check if any pattern matches the text
        boolean result = obscenePatterns.stream().anyMatch(pattern -> pattern.matcher(text).find());
        // Log a warning if obscene language is detected
        if (result) {
            logger.warn("Obscene language detected");
        }
        return result;
    }

    // Filters out obscene language from the given text
    public String filterObsceneLanguage(String text) {
        logger.info("Filtering obscene language in text");
        // Replace all matches of each pattern with "****"
        for (Pattern pattern : obscenePatterns) {
            text = pattern.matcher(text).replaceAll("****");
        }
        return text;
    }
}
