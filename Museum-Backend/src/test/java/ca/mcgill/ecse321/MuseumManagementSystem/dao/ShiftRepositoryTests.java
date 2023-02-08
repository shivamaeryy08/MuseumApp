package ca.mcgill.ecse321.MuseumManagementSystem.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Shift;

@SpringBootTest
public class ShiftRepositoryTests {
        
	@Autowired
	private ShiftRepository shiftRepository;
	@Autowired
	private EmployeeRepository employeeRepository5;

	@AfterEach
	public void clearDatabase() {
		shiftRepository.deleteAll();
		employeeRepository5.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadShift() {

	    //Association Test object
	    String username3 = "user3";
        String password3 = "pass3";
		Employee employee1 = new Employee();

		employee1.setUsername(username3);
		employee1.setPassword(password3);

		employeeRepository5.save(employee1);

        //Test Object
        Date shiftDate = new Date(2000);
        Time startTime = new Time(2);
        Time endTime = new Time(600);
		Shift shift = new Shift();
        shift.setShiftDate(shiftDate);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
		shift.setEmployee(employee1);

	    shift = shiftRepository.save(shift);

		long id = shift.getShiftID();
		
		shift = null;
		shift = shiftRepository.findShiftByShiftID(id);

        assertNotNull(shift);
		assertEquals(shiftDate.toString(), shift.getShiftDate().toString());

		assertNotNull(shift.getEmployee());
		assertEquals(username3, shift.getEmployee().getUsername());
    }

	@Test
	@Transactional
	public void testDeleteByEmployee() {

	    //Association Test object
	    String username3 = "user5";
        String password3 = "pass5";
		Employee employee1 = new Employee();

		employee1.setUsername(username3);
		employee1.setPassword(password3);

		employee1 = employeeRepository5.save(employee1);

        //Test Object
        Date shiftDate = new Date(2000);
        Time startTime = new Time(2);
        Time endTime = new Time(600);
		Shift shift = new Shift();
        shift.setShiftDate(shiftDate);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
		shift.setEmployee(employee1);

	    shift = shiftRepository.save(shift);
		long id = shift.getShiftID();

		shift = null;
		
		shiftRepository.deleteAllByEmployee(employee1);
		assertEquals(0, shiftRepository.findByEmployee(employee1).size());
		assert(shiftRepository.findShiftByShiftID(id) == null);
        
    }

	@Test
	public void testfindByEmployeeAndShiftDateBetween() {

	    //Association Test object
	    String username3 = "user6";
        String password3 = "pass3";
		Employee employee1 = new Employee();

		employee1.setUsername(username3);
		employee1.setPassword(password3);

		employeeRepository5.save(employee1);

        //Test Object
        Date shiftDate = new Date(2000);
        Time startTime = new Time(2);
        Time endTime = new Time(600);
		Shift shift = new Shift();
        shift.setShiftDate(shiftDate);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
		shift.setEmployee(employee1);

	    shift = shiftRepository.save(shift);

		long id = shift.getShiftID();
		
		List<Shift> shifts = shiftRepository.findByEmployeeAndShiftDateBetween(employee1, new Date(1000), new Date(5000));

        assertEquals(1, shifts.size());
		assertEquals(id, shifts.get(0).getShiftID());

    }
}
