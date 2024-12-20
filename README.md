import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBQRAccessGroupControllerTest {

    private UpdateBQRAccessGroupController controller; // Replace with your actual controller class
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        controller = new UpdateBQRAccessGroupController(); // Ensure your controller is properly initialized
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testHandleRequestWithValidInput() throws Exception {
        // Setting request parameters
        request.setParameter("strAccessGroupLobSelected", "targetAU");
        request.setParameter("selectedBqrList", "123,456");

        // Simulating session attributes
        request.getSession().setAttribute("UserDto", new UserDto("testUser"));

        // Mock DAO interactions (ensure DAO is injected into the controller via setter or constructor)
        CustomerCalcDao mockCustomerCalcDao = mock(CustomerCalcDao.class);
        controller.setCustomerCalcDao(mockCustomerCalcDao); // Replace with your actual injection mechanism

        when(mockCustomerCalcDao.updateBqrAu("123", "targetAU", "testUser")).thenReturn("Success");
        when(mockCustomerCalcDao.updateBqrAu("456", "targetAU", "testUser")).thenReturn("Success");

        // Call the method under test
        String result = controller.handleRequest(request);

        // Assertions
        assertNotNull(result);
        assertTrue(result.contains("BQR Access Group Update"));
        assertTrue(result.contains("123 Success"));
        assertTrue(result.contains("456 Success"));

        // Verify that the DAO was called twice
        verify(mockCustomerCalcDao, times(2)).updateBqrAu(anyString(), eq("targetAU"), eq("testUser"));
    }

    @Test
    void testHandleRequestWithInvalidInput() throws Exception {
        // Setting request parameters to invalid values
        request.setParameter("strAccessGroupLobSelected", null);
        request.setParameter("selectedBqrList", null);

        // Call the method under test
        String result = controller.handleRequest(request);

        // Assertions
        assertNotNull(result);
        assertTrue(result.contains("Error"));

        // No DAO interaction expected
        verifyNoInteractions(controller.getCustomerCalcDao());
    }
}