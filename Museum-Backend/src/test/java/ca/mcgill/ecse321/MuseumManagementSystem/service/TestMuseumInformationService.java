package ca.mcgill.ecse321.MuseumManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.MuseumInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;

@ExtendWith(MockitoExtension.class)
public class TestMuseumInformationService {
    @Mock
    private MuseumInformationRepository museumInformationRepository;

    @InjectMocks
    private MuseumInformationService museumInformationService;

    private static final double VISITOR_FEE = 15.0;
    private static final String MUSEUM_NAME = "testMuseumName";
    private static final String MONDAY_HOURS = "09:00-19:00";
    private static final String TUESDAY_HOURS = "09:00-19:00";
    private static final String WEDNESDAY_HOURS = "09:00-19:00";
    private static final String THURSDAY_HOURS = "09:00-19:00";
    private static final String FRIDAY_HOURS = "09:00-19:00";
    private static final String SATURDAY_HOURS = "09:00-19:00";
    private static final String SUNDAY_HOURS = "09:00-19:00";

    
    /* Error messages */
    private static final String INVALID_FEE = "Enter a positive fee \n";
    private static final String INVALID_HOURS = "Enter hours as HH:MM-HH:MM \n";


    @BeforeEach
    public void setMockOutput () {

       /*Museum information creation */
        Answer<?> returnMuseumInfo = (InvocationOnMock invocation) -> {
            MuseumInformation museumInformation = new MuseumInformation();
                        museumInformation.setVisitorFee (VISITOR_FEE);
                        museumInformation.setMuseumName(MUSEUM_NAME); 
                        museumInformation.setMondayHours(MONDAY_HOURS);
                        museumInformation.setTuesdayHours(TUESDAY_HOURS);
                        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
                        museumInformation.setThursdayHours(THURSDAY_HOURS);
                        museumInformation.setFridayHours(FRIDAY_HOURS);
                        museumInformation.setSaturdayHours(SATURDAY_HOURS);
                        museumInformation.setSundayHours(SUNDAY_HOURS);
                        List<MuseumInformation> iterable = new ArrayList<MuseumInformation>();
                        iterable.add(museumInformation);
                        return iterable;
        };
        lenient().when(museumInformationRepository.findAll()).thenAnswer(returnMuseumInfo);

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        lenient().when(museumInformationRepository.save(any(MuseumInformation.class))).thenAnswer(returnParameterAsAnswer);
        lenient().doNothing().when(museumInformationRepository).delete(any(MuseumInformation.class));
    }
    /*------------------------------------ Museum information tests --------------------------------------------------------------*/
    @Test
    public void testUpdateMuseumInformationValidFee () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationFee(museumInformation, 5);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals(5, museumInformation.getVisitorFee());
    }
    @Test
    public void testUpdateMuseumInformationInvalidFee () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationFee(museumInformation,-8);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(VISITOR_FEE, museumInformation.getVisitorFee());
        assertEquals(INVALID_FEE, error);
    }
    
    @Test
    public void testUpdateMuseumInformationMondayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationMondayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getMondayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidMondayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationMondayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(MONDAY_HOURS, museumInformation.getMondayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testUpdateMuseumInformationTuesdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationTuesdayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getTuesdayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidTuesdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationMondayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(TUESDAY_HOURS, museumInformation.getTuesdayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testUpdateMuseumInformationWednesdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationWednesdayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getWednesdayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidWednesdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationWednesdayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(WEDNESDAY_HOURS, museumInformation.getWednesdayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testUpdateMuseumInformationThursdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationThursdayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getThursdayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidThursdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationThursdayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(THURSDAY_HOURS, museumInformation.getThursdayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testUpdateMuseumInformationFridayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationFridayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getFridayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidFridayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationFridayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(FRIDAY_HOURS, museumInformation.getFridayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testUpdateMuseumInformationSaturdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationSaturdayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getSaturdayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidSaturdayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);;
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationSaturdayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(SATURDAY_HOURS, museumInformation.getSaturdayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testUpdateMuseumInformationSundayHours() {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        try {
            museumInformation = museumInformationService.updateMuseumInformationSundayHours(museumInformation,"09:00-17:00");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals("09:00-17:00", museumInformation.getSundayHours());
    }
    @Test
    public void testUpdateMuseumInformationInvalidSundayHours () {
        MuseumInformation museumInformation = new MuseumInformation();
        museumInformation.setVisitorFee(VISITOR_FEE);
        museumInformation.setMuseumName(MUSEUM_NAME);
        museumInformation.setMondayHours(MONDAY_HOURS);
        museumInformation.setTuesdayHours(TUESDAY_HOURS);
        museumInformation.setWednesdayHours(WEDNESDAY_HOURS);
        museumInformation.setThursdayHours(THURSDAY_HOURS);
        museumInformation.setFridayHours(FRIDAY_HOURS);
        museumInformation.setSaturdayHours(SATURDAY_HOURS);
        museumInformation.setSundayHours(SUNDAY_HOURS);
        String error = null;
        try {
            museumInformation = museumInformationService.updateMuseumInformationSundayHours(museumInformation,"");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(SUNDAY_HOURS, museumInformation.getSundayHours());
        assertEquals(INVALID_HOURS, error);
    }
    @Test
    public void testGetMuseumInformation() {
        MuseumInformation museumInformation = null;
        try {
            museumInformation = museumInformationService.getMuseumInformation();
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(museumInformation);
        assertEquals(MUSEUM_NAME, museumInformation.getMuseumName());
    }
}
