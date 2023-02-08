package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;
@SpringBootTest
public class OwnerRepositoryTests {
                
	@Autowired
	private OwnerRepository ownerRepository6;

	@AfterEach
	public void clearDatabase() {
		ownerRepository6.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadOwner() {

	    //Association Test object
	    String username4 = "user4";
        String password4 = "pass4";
		Owner owner6 = new Owner();

		owner6.setUsername(username4);
		owner6.setPassword(password4);

		ownerRepository6.save(owner6);
		
		owner6 = null;
		owner6 = ownerRepository6.findOwnerByUsername(username4);

        assertNotNull(owner6);
		assertEquals(password4, owner6.getPassword());
    }
}
