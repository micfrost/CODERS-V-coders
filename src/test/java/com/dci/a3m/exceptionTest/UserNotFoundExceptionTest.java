package com.dci.a3m.exceptionTest;

import com.dci.a3m.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundException() {
        String errorMessage = "User not found";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
