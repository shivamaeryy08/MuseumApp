package ca.mcgill.ecse321.MuseumManagementSystem.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.EmployeeRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OrderInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OwnerRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.ShiftRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.VisitorRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.EmployeeDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.OwnerDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.VisitorDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {
    
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired 
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private LoanRequestRepository requestRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private OrderInformationRepository orderInformationRepository;

    private static final String VISITOR_USERNAME = "testvisitor";
    private static final String EMPLOYEE_USERNAME = "employee";
    private static final String MMS_EMPLOYEE_USERNAME = "mms_employee";
    private static final String PASSWORD = "password";

    private static final String OWNER_PASSWORD = "admin2022";

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        orderInformationRepository.deleteAll();
        requestRepository.deleteAll();
        shiftRepository.deleteAll();
        visitorRepository.deleteAll();
        employeeRepository.deleteAll();
        /* Undo changes made to owner instance that is create on app startup */
        Owner owner = ownerRepository.findOwnerByUsername("admin");
        owner.setPassword(OWNER_PASSWORD);
        ownerRepository.save(owner);
    }

    /* --------------------------------------- Employee Integration Tests ---------------------------------- */

    @Test
    public void testCreateAndGetEmployee() {
        String username = testCreateEmployee();
        testGetEmployee(username);
    }

    @Test
    public void testCreateAndGetAllEmployees() {
        String username = testCreateEmployee();
        ResponseEntity<EmployeeDto[]> response = client.getForEntity("/employees/", EmployeeDto[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertEquals(1, response.getBody().length);
        assertEquals(username, response.getBody()[0].getUsername());
    }

    @Test
    public void testGetInvalidEmployee() {
        ResponseEntity<String> response = client.getForEntity("/employee/"+VISITOR_USERNAME, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
        assertEquals("Employee does not exist!", response.getBody(), "Response has correct message");
    }

    @Test
    public void testCreateAndLogInEmployee() {
        String username = testCreateEmployee();
        ResponseEntity<String> response = client.getForEntity("/login/E/"+username+"/"+PASSWORD+"/", String.class);

        assertNotNull(response);        
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("User authenticated", response.getBody(), "Response has correct message");
    }

    @Test
    public void testLogInOwner() {
        ResponseEntity<String> response = client.getForEntity("/login/A/admin/"+OWNER_PASSWORD+"/", String.class);
        
        assertNotNull(response);        
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("User authenticated", response.getBody(), "Response has correct message");
    }

    private String testCreateEmployee() {
        EmployeeDto employeeDto = new EmployeeDto(EMPLOYEE_USERNAME, PASSWORD);
        ResponseEntity<EmployeeDto> response = client.postForEntity("/employee", employeeDto, EmployeeDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(MMS_EMPLOYEE_USERNAME, response.getBody().getUsername(), "Response has correct username");

        return response.getBody().getUsername();
    }

    private void testGetEmployee(String username) {
        ResponseEntity<EmployeeDto> response = client.getForEntity("/employee/"+username, EmployeeDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(MMS_EMPLOYEE_USERNAME, response.getBody().getUsername(), "Response has correct username");
    }

    @Test
    public void testCreateInvalidEmployee() {
        ResponseEntity<String> response = client.postForEntity("/employee", new EmployeeDto(" ", " "), String.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateAndDeleteEmployee() {
        String username = testCreateEmployee();
        client.delete("/employee/"+username+"/");

        assertNull(employeeRepository.findEmployeeByUsername(username));
    }

    @Test
    public void testCreateAndUpdateEmployeeValidPassword() {
        String username = testCreateEmployee();
        EmployeeDto employeeDto = new EmployeeDto(username, OWNER_PASSWORD);
        ResponseEntity<EmployeeDto> response = client.postForEntity("/employee/"+username+"/updatePassword", employeeDto, EmployeeDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(OWNER_PASSWORD, response.getBody().getPassword(), "Response has correct email");
    }

    @Test
    public void testCreateAndUpdateEmployeeInvalidPassword() {
        String username = testCreateEmployee();
        EmployeeDto updatedEmployeeDto = new EmployeeDto(username, "5");
        ResponseEntity<String> response = client.postForEntity("/employee/"+username+"/updatePassword", updatedEmployeeDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
        assertEquals("Password must contain 7 or more characters!", response.getBody(), "Response has correct message");
    }


    /* --------------------------------------- Visitor Integration Tests ---------------------------------- */
    @Test
    public void testCreateAndGetVisitor() {
        String username = testCreateVisitor();
        testGetVisitor(username);
    }

    private String testCreateVisitor() {
        VisitorDto visitorDto = new VisitorDto(VISITOR_USERNAME, PASSWORD);
        ResponseEntity<VisitorDto> response = client.postForEntity("/visitor", visitorDto, VisitorDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(VISITOR_USERNAME, response.getBody().getUsername(), "Response has correct username");

        return response.getBody().getUsername();
    }

    private void testGetVisitor(String username) {
        ResponseEntity<VisitorDto> response = client.getForEntity("/visitor/"+username, VisitorDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(VISITOR_USERNAME, response.getBody().getUsername(), "Response has correct username");
    } 

    @Test
    public void testCreateInvalidVisitor() {
        ResponseEntity<String> response = client.postForEntity("/visitor", new VisitorDto(" ", " "), String.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
        assertEquals("Invalid username! Password must contain 7 or more characters!", response.getBody(), "Response has correct message");
    }

    @Test
    public void testGetInvalidVisitor() {
        ResponseEntity<String> response = client.getForEntity("/visitor/"+VISITOR_USERNAME, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
        assertEquals("Visitor does not exist!", response.getBody(), "Response has correct message");
    }

    @Test
    public void testCreateAndDeleteVisitor() {
        String username = testCreateVisitor();
        client.delete("/visitor/"+username+"/");
        assertNull(visitorRepository.findVisitorByUsername(username));
    }

    /* --------------------------------------- Owner Integration Tests ---------------------------------- */

    @Test
    public void testGetOwner() {
        ResponseEntity<OwnerDto> response = client.getForEntity("/admin/", OwnerDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("admin", response.getBody().getUsername(), "Response has correct username");
    }

    @Test
    public void testUpdateOwnerValidPassword() {
        OwnerDto updatedOwnerDto = new OwnerDto("admin", PASSWORD);
        ResponseEntity<OwnerDto> response = client.postForEntity("/admin/updatePassword", updatedOwnerDto, OwnerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(PASSWORD, response.getBody().getPassword(), "Response has correct password");
    }

    @Test
    public void testUpdateOwnerInvalidPassword() {
        OwnerDto updatedOwnerDto = new OwnerDto("admin", "5");
        ResponseEntity<String> response = client.postForEntity("/admin/updatePassword", updatedOwnerDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody());
        assertEquals("Password must contain 7 or more characters!", response.getBody(), "Response has correct message");
    }
}