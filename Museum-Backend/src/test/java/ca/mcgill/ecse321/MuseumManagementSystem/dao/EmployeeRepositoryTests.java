package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;
@SpringBootTest
public class EmployeeRepositoryTests {
            
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ShiftRepository shiftRepository;

	@AfterEach
	public void clearDatabase() {
		shiftRepository.deleteAll();
		employeeRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadEmployee() {

	    //Association Test object
	    String username = "user";
        String password = "pass";
		Employee employee = new Employee();

		employee.setUsername(username);
		employee.setPassword(password);

		employeeRepository.save(employee);
		
		employee = null;
		employee = employeeRepository.findEmployeeByUsername(username);

        assertNotNull(employee);
		assertEquals(password, employee.getPassword());
    }
}
