import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MyControllerTest {

    @Test
    void testHandleRequestForAdminUserWithSigFlagTrue() {
        // Arrange
        List<ADGroupDtls> adLOBIdList = new ArrayList<>();
        adLOBIdList.add(new ADGroupDtls("AD17", "DICA WBT_BRT_DISTRIBUTION_FINANCE", "Distribution Finance"));
        adLOBIdList.add(new ADGroupDtls("AD01", "DICA_WBT_BRT_ASSET_BASED_LENDING", "Asset Based Lending"));

        UserDto userDto = new UserDto();
        userDto.setRole("Admin");
        userDto.setSigFlag(true);
        userDto.setUserADGroups(adLOBIdList);

        ApplicationConstants.setAdGrpMap(new HashMap<>() {{
            put("AD17", new ADGroupDtls("AD17", "DICA WBT_BRT_DISTRIBUTION_FINANCE", "Distribution Finance"));
            put("AD01", new ADGroupDtls("AD01", "DICA_WBT_BRT_ASSET_BASED_LENDING", "Asset Based Lending"));
        }});

        BqrCalculatorWizardForm objwizForm = new BqrCalculatorWizardForm();
        BqrRecordSearchForm bqrRecordSearchForm = new BqrRecordSearchForm();

        // Act
        MyController controller = new MyController();
        controller.handleRequest(userDto, bqrRecordSearchForm, objwizForm);

        // Assert
        List<ADGroupDtls> resultGroups = bqrRecordSearchForm.getAccessGrouplobs();
        assertNotNull(resultGroups);
        assertEquals(2, resultGroups.size());
        assertEquals("Distribution Finance", resultGroups.get(0).getAdGrpArtLobName());
        assertEquals("Asset Based Lending", resultGroups.get(1).getAdGrpArtLobName());
    }

    @Test
    void testHandleRequestForNonAdminUser() {
        // Arrange
        List<ADGroupDtls> adLOBIdList = new ArrayList<>();
        adLOBIdList.add(new ADGroupDtls("AD17", "DICA WBT_BRT_DISTRIBUTION_FINANCE", "Distribution Finance"));

        UserDto userDto = new UserDto();
        userDto.setRole("User");
        userDto.setSigFlag(false);
        userDto.setUserADGroups(adLOBIdList);

        BqrCalculatorWizardForm objwizForm = new BqrCalculatorWizardForm();
        BqrRecordSearchForm bqrRecordSearchForm = new BqrRecordSearchForm();

        // Act
        MyController controller = new MyController();
        controller.handleRequest(userDto, bqrRecordSearchForm, objwizForm);

        // Assert
        List<ADGroupDtls> resultGroups = bqrRecordSearchForm.getAccessGrouplobs();
        assertNotNull(resultGroups);
        assertEquals(1, resultGroups.size());
        assertEquals("Distribution Finance", resultGroups.get(0).getAdGrpArtLobName());
    }

    @Test
    void testHandleRequestForNullUserDto() {
        // Arrange
        BqrCalculatorWizardForm objwizForm = new BqrCalculatorWizardForm();
        BqrRecordSearchForm bqrRecordSearchForm = new BqrRecordSearchForm();

        // Act
        MyController controller = new MyController();
        controller.handleRequest(null, bqrRecordSearchForm, objwizForm);

        // Assert
        assertNull(bqrRecordSearchForm.getAccessGrouplobs());
    }

    @Test
    void testHandleRequestForEmptyUserADGroups() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setRole("Admin");
        userDto.setSigFlag(true);
        userDto.setUserADGroups(Collections.emptyList());

        BqrCalculatorWizardForm objwizForm = new BqrCalculatorWizardForm();
        BqrRecordSearchForm bqrRecordSearchForm = new BqrRecordSearchForm();

        // Act
        MyController controller = new MyController();
        controller.handleRequest(userDto, bqrRecordSearchForm, objwizForm);

        // Assert
        List<ADGroupDtls> resultGroups = bqrRecordSearchForm.getAccessGrouplobs();
        assertNotNull(resultGroups);
        assertTrue(resultGroups.isEmpty());
    }
}