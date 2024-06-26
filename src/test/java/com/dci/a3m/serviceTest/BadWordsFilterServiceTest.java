package com.dci.a3m.serviceTest;

import com.dci.a3m.service.BadWordsFilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BadWordsFilterServiceTest {

    @InjectMocks
    private BadWordsFilterService badWordsFilterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock the obscene words property
        String obsceneWordsProperty = "fuck,bitch";
        ReflectionTestUtils.setField(badWordsFilterService, "obsceneWordsProperty", obsceneWordsProperty);
        badWordsFilterService.init();
    }

    @Test
    void testGetObsceneWords() {
        List<String> obsceneWords = badWordsFilterService.getObsceneWords();
        assertNotNull(obsceneWords);
        assertEquals(2, obsceneWords.size());
        assertTrue(obsceneWords.contains("fuck"));
        assertTrue(obsceneWords.contains("bitch"));
    }

    @Test
    void testContainsObsceneLanguage() {
        assertTrue(badWordsFilterService.containsObsceneLanguage("fuck this life bitch."));
        assertFalse(badWordsFilterService.containsObsceneLanguage("This is a clean sentence."));
    }

    @Test
    void testFilterObsceneLanguage() {
        String result = badWordsFilterService.filterObsceneLanguage("fuck this life bitch.");
        assertNotNull(result);
        assertEquals("**** this life ****.", result);

        result = badWordsFilterService.filterObsceneLanguage("This is a clean sentence.");
        assertEquals("This is a clean sentence.", result);
    }
}
