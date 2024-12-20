

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateBgrAccessGroupControllerTest {

    @InjectMocks
    private UpdateBgrAccessGroupController controller; // Assuming this is the class name

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private CustomerCalcDao customerCalcDao; // Mocked DAO for database interactions

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRequestWithValidInput() throws Exception {
        // Mock input parameters
        when(request.getParameter("strAccessGroupLobSelected")).thenReturn("targetAU");
        when(request.getParameter("selectedBqrList")).thenReturn("123,456");
        when(request.getSession()).thenReturn(session);

        UserDto userDto = new UserDto();
        userDto.setUserId("testUser");
        when(session.getAttribute("UserDto")).thenReturn(userDto);

        // Mock DAO behavior
        when(customerCalcDao.updateBqrAu("123", "targetAU", "testUser")).thenReturn("Success");
        when(customerCalcDao.updateBqrAu("456", "targetAU", "testUser")).thenReturn("Success");

        // Execute the method
        String result = controller.handleRequest(request);

        // Verify
        assertNotNull(result);
        assertTrue(result.contains("BQR Access Group Update"));
        verify(customerCalcDao, times(2)).updateBqrAu(anyString(), eq("targetAU"), eq("testUser"));
    }

    @Test
    void testHandleRequestWithInvalidInput() throws Exception {
        // Mock input parameters
        when(request.getParameter("strAccessGroupLobSelected")).thenReturn(null);

        // Execute the method
        String result = controller.handleRequest(request);

        // Verify
        assertNotNull(result);
        assertTrue(result.contains("Error"));
        verifyNoInteractions(customerCalcDao); // DAO should not be called
    }

    @Test
    void testHandleRequestWithException() throws Exception {
        // Mock input parameters
        when(request.getParameter("strAccessGroupLobSelected")).thenReturn("targetAU");
        when(request.getParameter("selectedBqrList")).thenReturn("123,456");
        when(request.getSession()).thenReturn(session);

        UserDto userDto = new UserDto();
        userDto.setUserId("testUser");
        when(session.getAttribute("UserDto")).thenReturn(userDto);

        // Mock DAO behavior to throw an exception
        when(customerCalcDao.updateBqrAu(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("DB Error"));

        // Execute the method
        String result = controller.handleRequest(request);

        // Verify
        assertNotNull(result);
        assertTrue(result.contains("Error"));
    }
}


#!/bin/bash

SERVER_NAME="$1"
LOG_FILE="$2"
TMP_FILE="/path/to/temp_file.txt"

# Regular expressions to search for error, exception, or warning patterns
ERROR_PATTERN="error"
EXCEPTION_PATTERN="exception"
WARNING_PATTERN="warning"

tail -f "$LOG_FILE" | while read -r line
do
    # Check for error, exception, or warning patterns and extract relevant information
    if [[ $line =~ $ERROR_PATTERN ]]; then
        echo "[$SERVER_NAME] Error: $line" >> "$TMP_FILE"
    elif [[ $line =~ $EXCEPTION_PATTERN ]]; then
        echo "[$SERVER_NAME] Exception: $line" >> "$TMP_FILE"
    elif [[ $line =~ $WARNING_PATTERN ]]; then
        echo "[$SERVER_NAME] Warning: $line" >> "$TMP_FILE"
    fi
done
