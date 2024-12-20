
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdateBQRAccessGroupControllerTest {

    @Autowired
    private UpdateBQRAccessGroupController controller;

    @MockBean
    private CustomerCalcDao customerCalcDao;  // Mock CustomerCalcDao

    @MockBean
    private JdbcTemplate jdbcTemplate;  // Mock JdbcTemplate

    private MockMvc mockMvc;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();  // Set up MockMvc

        // Mock JdbcTemplate behavior, ensuring it does not cause issues with null
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1); // Simulate successful update

        // Mock CustomerCalcDao's method to use mocked JdbcTemplate
        when(customerCalcDao.updateBqrAu(anyString(), anyString(), anyString())).thenReturn("Success");

        // Optionally mock UserDto behavior if needed
        UserDto mockUserDto = new UserDto("testUser");
        when(request.getSession().getAttribute("UserDto")).thenReturn(mockUserDto);
    }

    @Test
    void testHandleRequestWithValidInput() throws Exception {
        // Prepare the request and mock session attributes
        request = new MockHttpServletRequest();
        request.setParameter("strAccessGroupLobSelected", "targetAU");
        request.setParameter("selectedBqrList", "123,456");
        request.getSession().setAttribute("UserDto", new UserDto("testUser"));

        // Perform the request to the controller
        mockMvc.perform(post("/updatebgraccessgroup")
            .param("strAccessGroupLobSelected", "targetAU")
            .param("selectedBqrList", "123,456")
            .sessionAttr("UserDto", new UserDto("testUser")))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("BQR Access Group Update")));

        // Verify interactions with JdbcTemplate
        verify(jdbcTemplate, times(2)).update(anyString(), any(Object[].class)); // Verify the update method was called for each BQR ID
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LogScanner {
    public static void main(String[] args) {
        String serverName = args[0];
        String tmpFilePath = "/path/to/temp_file.txt";

        Set<String> uniqueErrors = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(tmpFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Store unique errors, warnings, or exceptions
                uniqueErrors.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Construct email notification with unique errors
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Server: ").append(serverName).append("\n");
        emailContent.append("Unique Errors, Warnings, or Exceptions:\n");
        for (String error : uniqueErrors) {
            emailContent.append(error).append("\n");
        }

        // TODO: Use JavaMail or any email library to send the email notification
    }
}
