import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EnrollControllerTest {

    @Mock
    private PUserSessionSpring pUserSessionSpring;

    @Mock
    private Model model;

    @Mock
    private MessageResolver messageResolver;

    @Mock
    private Provider mockProvider;  // Mock Provider directly

    @InjectMocks
    private EnrollController enrollController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowTerms() {
        // Create a mock PUserSession and Facility
        PUserSession mockUserSession = new PUserSession();
        Facility mockFacility = new Facility(); // Adjust Facility class based on your actual implementation
        mockUserSession.setProvider(mockProvider);

        // Mock the behavior of PUserSessionSpring
        when(pUserSessionSpring.getPUserSession()).thenReturn(mockUserSession);

        // Mock the behavior of MessageResolver
        String MSG_ERROR = "yourErrorMessage";
        when(messageResolver.getLocalizedMessage(eq(MSG_ERROR), any())).thenReturn("Mocked error message");

        // Mock the behavior of getFacilityList
        ArrayList<Facility> mockFacilityList = new ArrayList<>();
        mockFacilityList.add(mockFacility);
        when(mockProvider.getFacilityList()).thenReturn(mockFacilityList);

        // Call the method to be tested
        String result = enrollController.showTerms(model, mock(HttpServletRequest.class));

        // If EnrollProgress is null or not validated, verify that the errorMsg attribute is set
        if (mockUserSession.getEnrollProgress() == null || !mockUserSession.getEnrollProgress().isValidate()) {
            verify(model).addAttribute(eq("errorMsg"), any());
        }

        // Verify that the model.addAttribute() method is called with the expected attribute names
        verify(model).addAttribute(eq("facilityList"), any());

        // Assert the expected return value
        assertEquals("OK", result);

        // Capture the argument passed to model.addAttribute("facilityList", ...)
        ArgumentCaptor<ArrayList<Facility>> facilityListCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(model).addAttribute(eq("facilityList"), facilityListCaptor.capture());

        // Assert the contents of the captured argument
        assertEquals(mockFacilityList, facilityListCaptor.getValue());
    }
}
