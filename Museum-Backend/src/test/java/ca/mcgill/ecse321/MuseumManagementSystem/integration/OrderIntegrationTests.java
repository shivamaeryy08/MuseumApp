package ca.mcgill.ecse321.MuseumManagementSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.OrderInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.VisitorRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.OrderInformationDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.VisitorDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderIntegrationTests {
    
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private OrderInformationRepository orderInformationRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    private static final String USERNAME1 = "username1";
    private static final String EMAIL1 = "test@email.com";
  
    private static final Integer AMOUNT_TEST = 1;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        orderInformationRepository.deleteAll();
        visitorRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetOrderInformation() {
        Long orderID = testCreateOrderInformation();
        testGetOrderInformationByOrderID(orderID);
    }

    @Test
    public void testCreateInvalidOrderInformation() {
        var response = client.postForEntity("/order", new OrderInformationDto(0, createVisitorDto(USERNAME1, EMAIL1)), String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Order amount cannot be Zero! ", response.getBody());
    }

    @Test
    public void testCreateAndGetOrderInformationByVisitor() {
        testCreateOrderInformation();
        var response = client.getForEntity("/username1/orders", OrderInformationDto[].class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
        OrderInformationDto[] orderInformation = response.getBody();
		assertEquals(USERNAME1, orderInformation[0].getVisitor().getUsername(), "Response has correct employee");
    }

    @Test
    public void testGetInvalidOrderInformation() {
        var response = client.getForEntity("/orders/"+Long.MAX_VALUE+"/", String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("An order with the given ID cannot be found!", response.getBody());
    }

    @Test
    public void testCreateAndGetOrderInformationByInvalidVisitor() {
        testCreateOrderInformation();
        var response = client.getForEntity("/invaliduser/orders", String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    }

    @Test
    public void testGetAllOrders() {
        testCreateOrderInformation();
        var response = client.getForEntity("/orders", OrderInformationDto[].class);
        assertNotNull(response);
        assertEquals(1, response.getBody().length);
        assertEquals(AMOUNT_TEST, response.getBody()[0].getAmount());
    }

    private Long testCreateOrderInformation() {
        OrderInformationDto orderInformationDto = new OrderInformationDto(AMOUNT_TEST, createVisitorDto(USERNAME1,EMAIL1));

        var response = client.postForEntity("/order", orderInformationDto, OrderInformationDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(AMOUNT_TEST, response.getBody().getAmount(), "Response has correct amount");
        assertTrue(response.getBody().getOrderID() > 0, "Response has valid ID");

        return response.getBody().getOrderID();
    }

    private void testGetOrderInformationByOrderID(Long orderID) {
        var response = client.getForEntity("/orders/"+orderID+"/", OrderInformationDto.class);

        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(AMOUNT_TEST, response.getBody().getAmount(), "Response has correct amount");
        assertEquals(orderID, response.getBody().getOrderID());
    }

    /* --------------------- Helper Methods ------------------------------------*/

    private VisitorDto createVisitorDto(String username, String email) {
     
        Visitor visitor= new Visitor();
        visitor.setUsername(username);
        visitor.setPassword("password");
        visitor = visitorRepository.save(visitor);
        VisitorDto visitorDto = new VisitorDto(visitor.getUsername(), "password");
        return visitorDto;
    }
}