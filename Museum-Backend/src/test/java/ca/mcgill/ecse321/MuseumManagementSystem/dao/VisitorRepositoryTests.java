package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;

@SpringBootTest
public class VisitorRepositoryTests {
               
	@Autowired
	private VisitorRepository visitorRepository1;

	@Autowired
	private LoanRequestRepository requestRepository;

	@Autowired
	private OrderInformationRepository orderInformationRepository;

	@AfterEach
	public void clearDatabase() {
		orderInformationRepository.deleteAll();
		requestRepository.deleteAll();
		visitorRepository1.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadVisitor() {

	    //Test object
	    String username1 = "user39";
        String password1 = "pass";
		Visitor visitor1 = new Visitor();

		visitor1.setUsername(username1);
		visitor1.setPassword(password1);

		visitorRepository1.save(visitor1);
		
		visitor1 = null;
		visitor1 = visitorRepository1.findVisitorByUsername(username1);

		assertNotNull(visitor1);
		assertEquals(username1, visitor1.getUsername());
    }
}
