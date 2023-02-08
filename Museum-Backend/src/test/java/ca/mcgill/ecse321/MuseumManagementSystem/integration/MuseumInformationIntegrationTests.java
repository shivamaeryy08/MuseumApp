package ca.mcgill.ecse321.MuseumManagementSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.MuseumInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.MuseumInformationDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;

@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
public class MuseumInformationIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private MuseumInformationRepository museumInformationRepository;

    @Autowired

    private static final double VISITOR_FEE = 10.50;
    private static final String MUSEUM_NAME = "Museum";
    private static final String WEEKDAY_HOURS = "09:00-17:00";
    private static final String WEEKEND_HOURS = "10:00-16:00";
     
    private static final Date HOLIDAY_DATE = Date.valueOf("2023-11-07");
    private static final String HOLIDAY_HOURS = "12:00-18:00" ;

     /* Error message */
     private static final String INVALID_HOURS = "Enter hours as HH:MM-HH:MM \n";


    @BeforeEach
    @AfterEach
    public void clearDatabase () {
        //Reset museum information instance to attributes it was originally created upon initialization
        MuseumInformation museum = museumInformationRepository.findMuseumInformationByMuseumName("Museum");
        museum.setMondayHours(WEEKDAY_HOURS);
        museum.setTuesdayHours(WEEKDAY_HOURS);
        museum.setWednesdayHours(WEEKDAY_HOURS);
        museum.setThursdayHours(WEEKDAY_HOURS);
        museum.setFridayHours(WEEKDAY_HOURS);
        museum.setSaturdayHours(WEEKEND_HOURS);
        museum.setSundayHours(WEEKEND_HOURS);
        museum.setMuseumName(MUSEUM_NAME);
        museum.setVisitorFee(VISITOR_FEE);
        museumInformationRepository.save(museum);
    }
    
    /* --------------------- Museum Information Tests ----------------------------------------------------------------*/
    @Test
    public void testGetMuseumInformation () {
        ResponseEntity <MuseumInformationDto> response = client.getForEntity("/museumInformation/", MuseumInformationDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        assertEquals(response.getBody().getMuseumName(), MUSEUM_NAME);
    }

    @Test
    public void testUpdateVisitorFee () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (12.0,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateVisitorFee", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(12.0, response.getBody().getVisitorFee(), "Response has correct visitor fee");

    }
    @Test
    public void testUpdateMondayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,"09:00-10:00",WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateMondayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("09:00-10:00", response.getBody().getMondayHours(), "Response has updated monday hours");

    }
    @Test
    public void testUpdateInvalidMondayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,"00-10:00",WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateMondayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

    @Test
    public void testUpdateTuesdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKEND_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateTuesdayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(WEEKEND_HOURS, response.getBody().getTuesdayHours(), "Response has updated tuesday hours");

    }
    @Test
    public void testUpdateInvalidTuesdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,"CC",WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateTuesdayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

    @Test
    public void testUpdateWednesdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateWednesdayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(WEEKEND_HOURS, response.getBody().getWednesdayHours(), "Response has updated wednesday hours");

    }
    @Test
    public void testUpdateInvalidWednesdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,"WEEKDAY_HOURS",WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateWednesdayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

    @Test
    public void testUpdateThursdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateThursdayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(WEEKEND_HOURS, response.getBody().getThursdayHours(), "Response has updated thursday hours");

    }
    @Test
    public void testUpdateInvalidThursdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,"c",WEEKDAY_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateThursdayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

    @Test
    public void testUpdateFridayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS,WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateFridayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(WEEKEND_HOURS, response.getBody().getFridayHours(), "Response has updated friday hours");

    }
    @Test
    public void testUpdateInvalidFridayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,"c",WEEKEND_HOURS, WEEKEND_HOURS);
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateFridayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

    @Test
    public void testUpdateSaturdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS, WEEKEND_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateSaturdayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(WEEKDAY_HOURS, response.getBody().getSaturdayHours(), "Response has updated saturday hours");

    }
    @Test
    public void testUpdateInvalidSaturdayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,"v", WEEKEND_HOURS);
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateSaturdayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

    @Test
    public void testUpdateSundayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS, WEEKDAY_HOURS);
        ResponseEntity<MuseumInformationDto> response = client.postForEntity("/museumInformation/updateSundayHours", updatedMuseumInformationDto, MuseumInformationDto.class);
        assertNotNull(response);assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(WEEKDAY_HOURS, response.getBody().getSundayHours(), "Response has updated sunday hours");

    }
    @Test
    public void testUpdateInvalidSundayHours () {
        MuseumInformationDto updatedMuseumInformationDto = new MuseumInformationDto (VISITOR_FEE,MUSEUM_NAME,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKDAY_HOURS,WEEKEND_HOURS, "WEEKEND_HOURS");
        ResponseEntity<String> response = client.postForEntity("/museumInformation/updateSundayHours", updatedMuseumInformationDto, String.class);
        assertNotNull(response);assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(INVALID_HOURS, response.getBody()); //expected from service
    }

     /* --------------------- Helper Methods ----------------------------------------------------------------*/
     private MuseumInformationDto getMuseumInformationDto() {
        return new MuseumInformationDto(VISITOR_FEE, MUSEUM_NAME, WEEKDAY_HOURS, WEEKDAY_HOURS, WEEKDAY_HOURS, WEEKDAY_HOURS, WEEKDAY_HOURS, WEEKEND_HOURS, WEEKEND_HOURS);
    }
}