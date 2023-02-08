package ca.mcgill.ecse321.MuseumManagementSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;

import org.apache.coyote.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.EmployeeRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.ShiftRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.EmployeeDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.ShiftDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShiftIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Date SHIFT_DATE = Date.valueOf("2025-12-05");
    private static final Time START_TIME = Time.valueOf("10:00:00");
    private static final Time END_TIME = Time.valueOf("14:00:00");
    private static final Date DATE_BEFORE = Date.valueOf("2025-12-03");
    private static final Date DATE_AFTER = Date.valueOf("2025-12-07");
    private static final String USERNAME1 = "mms_employee1";
    private static final String USERNAME2 = "mms_employee2";
    private static final String EMAIL1 = "emp1@test.com";
    private static final String EMAIL2 = "emp2@test.com";

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        shiftRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetShift() {
        Long shiftID = testCreateShift();
        testGetShift(shiftID);
    }

    @Test
    public void testCreateAndGetAllShifts() {
        Long shiftID = testCreateShift();
        ResponseEntity<ShiftDto[]> response = client.getForEntity("/shifts/", ShiftDto[].class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        assertEquals(1, response.getBody().length);
		assertEquals(SHIFT_DATE.toString(), response.getBody()[0].getShiftDate().toString(), "Response has correct shift date");
        assertEquals(shiftID, response.getBody()[0].getShiftID(), "Response has correct ID");
    }

    @Test
    public void testCreateInvalidShift() {
        var response = client.postForEntity("/shift", new ShiftDto(SHIFT_DATE, END_TIME, START_TIME, createEmployeeDto(USERNAME1, EMAIL1)), String.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("End time must be after start time!", response.getBody(), "Response has correct message");
    }

    @Test
    public void testCreateAndUpdateShiftValidEmployee() {
        Long shiftID = testCreateShift();
        //Employee the shift is being updated to
        createEmployeeDto(USERNAME2, EMAIL2);
        var response = client.getForEntity("/shift/"+shiftID+"/updateEmployee/"+USERNAME2, ShiftDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(shiftID, response.getBody().getShiftID(), "Response has correct shift id");
        assertEquals(USERNAME2, response.getBody().getEmployee().getUsername(), "Response has correct updated employee");
    }

    @Test
    public void testCreateAndUpdateShiftInvalidEmployee() {
        Long shiftID = testCreateShift();
        var response = client.getForEntity("/shift/"+shiftID+"/updateEmployee/  ", String.class);

        assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        assertEquals("Username cannot be empty!", response.getBody(), "Response has correct message");
    }

    private Long testCreateShift() {
        ShiftDto shiftDto = new ShiftDto(SHIFT_DATE, START_TIME, END_TIME, createEmployeeDto(USERNAME1, EMAIL1));
        var response = client.postForEntity("/shift", shiftDto, ShiftDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(SHIFT_DATE.toString(), response.getBody().getShiftDate().toString(), "Response has correct shift date");
        assertTrue(response.getBody().getShiftID() > 0, "Response has valid ID");

        return response.getBody().getShiftID();
    }

    private void testGetShift(Long shiftID) {
        var response = client.getForEntity("/shift/"+shiftID, ShiftDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(SHIFT_DATE.toString(), response.getBody().getShiftDate().toString(), "Response has correct shift date");
        assertEquals(shiftID, response.getBody().getShiftID(), "Response has correct ID");
    }

    @Test
    public void testCreateAndDeleteShift() {
        Long shiftID = testCreateShift();
        client.delete("/shift/"+shiftID+"/");
        assertNull(shiftRepository.findShiftByShiftID(shiftID));
    }

    @Test
    public void testCreateAndGetEmployeeShiftOnDate() {
        testCreateShift();
        String date = SHIFT_DATE.toString();
        var response = client.getForEntity("/shifts/"+USERNAME1+"/"+date, ShiftDto[].class);
        
        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        ShiftDto[] shifts = response.getBody();
        assertEquals(date, shifts[0].getShiftDate().toString(), "Response has correct date");
        assertEquals(USERNAME1, shifts[0].getEmployee().getUsername(), "Response has correct employee");
    }

    @Test
    public void testCreateAndGetEmployeeShiftBetweenDates() {
        testCreateShift();
        String date1 = DATE_BEFORE.toString();
        String date2 = DATE_AFTER.toString();
        var response = client.getForEntity("/shifts/"+USERNAME1+"/"+date1+"/"+date2, ShiftDto[].class);
        
        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        ShiftDto[] shifts = response.getBody();
        assertEquals(SHIFT_DATE.toString(), shifts[0].getShiftDate().toString(), "Response has correct date");
        assertEquals(USERNAME1, shifts[0].getEmployee().getUsername(), "Response has correct employee");
    }

    @Test
    public void testCreateAndGetEmployeeShiftBetweenInvalidDates() {
        testCreateShift();
        String date1 = DATE_BEFORE.toString();
        String date2 = DATE_AFTER.toString();
        var response = client.getForEntity("/shifts/"+USERNAME1+"/"+date2+"/"+date1, String.class);
        
        assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        assertEquals("End date must be after start date!", response.getBody(), "Response has correct message");
    }

    /* --------------------- Helper Methods ------------------------------------*/
    private EmployeeDto createEmployeeDto(String username, String email) {
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword("password");
        employee = employeeRepository.save(employee);
        EmployeeDto employeeDto = new EmployeeDto(employee.getUsername(), employee.getPassword());
        return employeeDto;
    }
}