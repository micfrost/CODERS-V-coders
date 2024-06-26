package com.dci.a3m.ControllerMVCTest;


import com.dci.a3m.controller.HomeControllerMVC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class HomeControllerMVCTest {

    @InjectMocks
    private HomeControllerMVC homeControllerMVC;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHome() {
        String viewName = homeControllerMVC.home();
        assertEquals("redirect:/login-success", viewName);
    }

    @Test
    public void testHomehome() {
        String viewName = homeControllerMVC.homehome();
        assertEquals("redirect:/login-success", viewName);
    }
}
