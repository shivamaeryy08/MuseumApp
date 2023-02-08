package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.OrderInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;

@SpringBootTest
public class OrderInformationRepositoryTests {
           
	@Autowired
	private OrderInformationRepository orderInformationRepository;
	
	@Autowired
	private VisitorRepository visitorRepository2;

	@AfterEach
	public void clearDatabase() {
		orderInformationRepository.deleteAll();
		visitorRepository2.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadOrderInformation() {

	    //Association Test object
	    String username2 = "user2";
        String password2 = "pass2";
		Visitor visitor2 = new Visitor();

		visitor2.setUsername(username2);
		visitor2.setPassword(password2);

		visitorRepository2.save(visitor2);

        //Test Object
        Date orderDate = new Date(2000);
        int amount = 5;
		double totalPrice = amount * 10.50;
		OrderInformation orderInformation = new OrderInformation();
        orderInformation.setOrderDate(orderDate);
        orderInformation.setAmount(amount);
		orderInformation.setTotalPrice(totalPrice);
		orderInformation.setVisitor(visitor2);

	    orderInformation = orderInformationRepository.save(orderInformation);

		long id = orderInformation.getOrderID();
		
		orderInformation = null;
		orderInformation = orderInformationRepository.findOrderInformationByOrderID(id);

        assertNotNull(orderInformation);
		assertEquals(orderDate.toString(), orderInformation.getOrderDate().toString());

		assertNotNull(orderInformation.getVisitor());
		assertEquals(username2, orderInformation.getVisitor().getUsername());
    }
}
