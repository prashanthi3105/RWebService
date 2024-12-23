import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class YourControllerTest {

    @InjectMocks
    private YourController controller; // Replace with the actual controller class name

    @Mock
    private HttpSession session;

    @Mock
    private BQRRecordSearchForm bqrRecordSearchForm; // Mock form object

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = new UserDto(); // Initialize a UserDto object
    }

    @Test
    void testAdminUserWithSigFlag() {
        // Arrange
        userDto.setRole("Admin");
        userDto.setSigFlag(true);

        Map<String, ADGroupDtls> adGrpMap = new HashMap<>();
        adGrpMap.put("group1", new ADGroupDtls("Group1", "Description1"));
        adGrpMap.put("group2", new ADGroupDtls("Group2", "Description2"));

        ApplicationConstants.setAdGrpMap(adGrpMap); // Assume setter exists for ApplicationConstants.getAdGrpMap()

        when(session.getAttribute("UserDto")).thenReturn(userDto);

        // Act
        controller.handleSessionAttributes(session, bqrRecordSearchForm);

        // Assert
        verify(bqrRecordSearchForm).setAccessGrouplobs(anyList());
        List<ADGroupDtls> adGroups = new ArrayList<>(adGrpMap.values());
        assertEquals(adGroups.size(), 2);
    }

    @Test
    void testNonAdminUserWithADGroups() {
        // Arrange
        userDto.setRole("User");
        userDto.setSigFlag(false);

        List<ADGroupDtls> userAdGroups = Arrays.asList(
                new ADGroupDtls("GroupA", "DescriptionA"),
                new ADGroupDtls("GroupB", "DescriptionB")
        );
        userDto.setUserADGroups(userAdGroups);

        when(session.getAttribute("UserDto")).thenReturn(userDto);

        // Act
        controller.handleSessionAttributes(session, bqrRecordSearchForm);

        // Assert
        verify(bqrRecordSearchForm).setAccessGrouplobs(userAdGroups);
    }

    @Test
    void testUserDtoNull() {
        // Arrange
        when(session.getAttribute("UserDto")).thenReturn(null);

        // Act
        controller.handleSessionAttributes(session, bqrRecordSearchForm);

        // Assert
        verify(bqrRecordSearchForm, never()).setAccessGrouplobs(anyList());
    }

    @Test
    void testUserDtoWithNullAdGroups() {
        // Arrange
        userDto.setRole("User");
        userDto.setSigFlag(false);
        userDto.setUserADGroups(null);

        when(session.getAttribute("UserDto")).thenReturn(userDto);

        // Act
        controller.handleSessionAttributes(session, bqrRecordSearchForm);

        // Assert
        verify(bqrRecordSearchForm, never()).setAccessGrouplobs(anyList());
    }

    @Test
    void testAdminUserWithoutSigFlag() {
        // Arrange
        userDto.setRole("Admin");
        userDto.setSigFlag(false);

        when(session.getAttribute("UserDto")).thenReturn(userDto);

        // Act
        controller.handleSessionAttributes(session, bqrRecordSearchForm);

        // Assert
        verify(bqrRecordSearchForm, never()).setAccessGrouplobs(anyList());
    }
}