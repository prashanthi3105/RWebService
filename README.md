
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;

import java.util.StringTokenizer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateBQRAccessGroupControllerTest {

    @InjectMocks
    private UpdateBQRAccessGroupController controller;

    @Mock
    private CustomerCalcDao customerCalcDao;

    @Mock
    private HttpSession session;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testHandleRequestWithValidInput() throws Exception {
        // Arrange
        request.setParameter("strAccessGroupLobSelected", "LOB123");
        request.setParameter("selectedBqrList", "BQR1,BQR2");
        UserDto mockUserDto = new UserDto("testUser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("UserDto")).thenReturn(mockUserDto);

        when(customerCalcDao.updateBqrAu(anyString(), anyString(), anyString()))
            .thenReturn("Updated");

        // Act
        String result = controller.handleRequest(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("BQR Access Group Update"));
        verify(customerCalcDao, times(2)).updateBqrAu(anyString(), eq("LOB123"), eq("testUser"));
    }

    @Test
    void testHandleRequestWithNullSelectedBqrList() throws Exception {
        // Arrange
        request.setParameter("strAccessGroupLobSelected", "LOB123");
        request.setParameter("selectedBqrList", null);
        UserDto mockUserDto = new UserDto("testUser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("UserDto")).thenReturn(mockUserDto);

        // Act
        String result = controller.handleRequest(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.contains("Updated"));
        verify(customerCalcDao, never()).updateBqrAu(anyString(), anyString(), anyString());
    }

    @Test
    void testHandleRequestWithNullUserDto() throws Exception {
        // Arrange
        request.setParameter("strAccessGroupLobSelected", "LOB123");
        request.setParameter("selectedBqrList", "BQR1,BQR2");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("UserDto")).thenReturn(null);

        // Act
        String result = controller.handleRequest(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.contains("Updated"));
        verify(customerCalcDao, never()).updateBqrAu(anyString(), anyString(), anyString());
    }

    @Test
    void testHandleRequestWithInvalidTargetAu() throws Exception {
        // Arrange
        request.setParameter("strAccessGroupLobSelected", "");
        request.setParameter("selectedBqrList", "BQR1,BQR2");
        UserDto mockUserDto = new UserDto("testUser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("UserDto")).thenReturn(mockUserDto);

        // Act
        String result = controller.handleRequest(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.contains("Updated"));
        verify(customerCalcDao, never()).updateBqrAu(anyString(), anyString(), anyString());
    }

    @Test
    void testHandleRequestWithEmptySelectedBqrList() throws Exception {
        // Arrange
        request.setParameter("strAccessGroupLobSelected", "LOB123");
        request.setParameter("selectedBqrList", "");
        UserDto mockUserDto = new UserDto("testUser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("UserDto")).thenReturn(mockUserDto);

        // Act
        String result = controller.handleRequest(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.contains("Updated"));
        verify(customerCalcDao, never()).updateBqrAu(anyString(), anyString(), anyString());
    }

    @Test
    void testHandleRequestWithSqlInjectionFilter() throws Exception {
        // Arrange
        request.setParameter("strAccessGroupLobSelected", "LOB123");
        request.setParameter("selectedBqrList", "BQR1,BQR2");
        UserDto mockUserDto = new UserDto("testUser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("UserDto")).thenReturn(mockUserDto);

        // Mock SQL injection filter behavior
        when(BarUtil.sqlInjectionFilter(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(EncoderUtil.forJava(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerCalcDao.updateBqrAu(anyString(), anyString(), anyString())).thenReturn("Updated");

        // Act
        String result = controller.handleRequest(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("BQR Access Group Update"));
        verify(customerCalcDao, times(2)).updateBqrAu(anyString(), eq("LOB123"), eq("testUser"));
    }
}




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