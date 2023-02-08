package ca.mcgill.ecse321.MuseumManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.VisitorRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OwnerRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.ShiftRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OrderInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.EmployeeRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;

@ExtendWith(MockitoExtension.class)
public class TestUserService {
    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private OrderInformationRepository orderInformationRepository;

    @Mock
    private LoanRequestRepository loanRequestRepository;

    @InjectMocks
    private UserService userService;

    private static final String UNIQUE_USERNAME = "uniqueuser";

    private static final String VISITOR_USERNAME = "testvisitor";
    private static final String VISITOR_PASSWORD = "testvisitorpass";
    private static final String VISITOR_USERNAME2 = "testvisitor2";

    private static final String EMPLOYEE_USERNAME = "testemployee";
    private static final String EMPLOYEE_USERNAME_MMS = "mms_testemployee";
    private static final String EMPLOYEE_USERNAME_MMS2 = "mms_teste2";
    private static final String EMPLOYEE_PASSWORD = "testemployeepass";
    private static final String EMPLOYEE_PREFIX = "mms_";

    private static final String OWNER_PASSWORD = "testownerpass";

    private static final String BAD_PASSWORD = "1";

    /* Error messages */
    private static final String INVALID_USERNAME = "Invalid username! ";
    private static final String USERNAME_TAKEN = "Username is taken! ";
    private static final String SHORT_PASSWORD = "Password must contain 7 or more characters!";
    private static final String NO_EMPLOYEE = "Employee does not exist!";
    private static final String NO_VISITOR = "Visitor does not exist!";
    private static final String EMPTY_USERNAME = "Username cannot be empty!";
    private static final String NULL_EMPLOYEE = "Must specify employee!";

    @BeforeEach
    public void setMockOutput() {

        lenient().when(visitorRepository.findVisitorByUsername(anyString())).thenAnswer(
            (InvocationOnMock invocation) -> {
                if(invocation.getArgument(0).equals(VISITOR_USERNAME)) {
                    Visitor visitor = new Visitor();
                    visitor.setUsername(VISITOR_USERNAME);
                    visitor.setPassword(VISITOR_PASSWORD);
                    return visitor;
                } else if (invocation.getArgument(0).equals(VISITOR_USERNAME2)) {
                    Visitor visitor = new Visitor();
                    visitor.setUsername(VISITOR_USERNAME2);
                    visitor.setPassword(VISITOR_PASSWORD);
                    return visitor;
                }
                else {
                    return null;
                }
            }
        );
        lenient().when(employeeRepository.findEmployeeByUsername(anyString())).thenAnswer(
            (InvocationOnMock invocation) -> {
                if(invocation.getArgument(0).equals(EMPLOYEE_USERNAME_MMS)) {
                    Employee employee = new Employee();
                    employee.setUsername(EMPLOYEE_USERNAME_MMS);
                    employee.setPassword(EMPLOYEE_PASSWORD);
                    return employee;
                } else if(invocation.getArgument(0).equals(EMPLOYEE_USERNAME_MMS2)) {
                    Employee employee = new Employee();
                    employee.setUsername(EMPLOYEE_USERNAME_MMS2);
                    employee.setPassword(EMPLOYEE_PASSWORD);
                    return employee;
                }
                else {
                    return null;
                }
            }
        );
        lenient().when(ownerRepository.findOwnerByUsername(anyString())).thenAnswer(
            (InvocationOnMock invocation) -> {
                if(invocation.getArgument(0).equals("admin")) {
                    Owner owner = new Owner();
                    owner.setUsername("admin");
                    owner.setPassword(OWNER_PASSWORD);
                    return owner;
                } else {
                    return null;
                }
            }
        );
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
        lenient().when(visitorRepository.save(any(Visitor.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(employeeRepository.save(any(Employee.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(ownerRepository.save(any(Owner.class))).thenAnswer(returnParameterAsAnswer);
        lenient().doNothing().when(employeeRepository).delete(any(Employee.class));
        lenient().doNothing().when(visitorRepository).delete(any(Visitor.class));
        lenient().doNothing().when(shiftRepository).deleteAllByEmployee(any(Employee.class));
        lenient().doNothing().when(loanRequestRepository).deleteAllByRequester(any(Visitor.class));
        lenient().doNothing().when(orderInformationRepository).deleteAllByVisitor(any(Visitor.class));
    }

    /* -----------------------------Employee tests ------------------------------------------------------*/
    @Test
    public void testCreateEmployee() {
        assertEquals(0, userService.getAllEmployees().size());

        Employee employee = null;
        try {
            employee = userService.createEmployee(UNIQUE_USERNAME, EMPLOYEE_PASSWORD);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(employee);
        assertEquals(EMPLOYEE_PREFIX+UNIQUE_USERNAME, employee.getUsername());
        assertEquals(EMPLOYEE_PASSWORD, employee.getPassword());
    }

    @Test
    public void testCreateEmployeeInvalidUsername() {
        String error = "";
        Employee employee = null;
        try{
            employee = userService.createEmployee("", EMPLOYEE_PASSWORD );
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(INVALID_USERNAME, error);
        assertNull(employee);
    }

    @Test
    public void testCreateEmployeeUsernameTaken() {
        String error = "";
        Employee employee = null;
        try{
            employee = userService.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(USERNAME_TAKEN, error);
        assertNull(employee);
    }

    @Test
    public void testCreateEmployeeShortPassword() {
        String error = "";
        Employee employee = null;
        try{
            employee = userService.createEmployee(UNIQUE_USERNAME, BAD_PASSWORD);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(SHORT_PASSWORD, error);
        assertNull(employee);
    }

    @Test
    public void testGetExistingEmployeeByUsername(){
        Employee employee = null;
        try {
            employee = userService.getEmployeeByUsername(EMPLOYEE_USERNAME_MMS2);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(employee);
        assertEquals(EMPLOYEE_USERNAME_MMS2, employee.getUsername());
    }

    @Test
    public void testGetNonExistingEmployeeByUsername() {
        String error = "";
        Employee employee = null;
        try {
            employee = userService.getEmployeeByUsername(UNIQUE_USERNAME);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(NO_EMPLOYEE, error);
        assertNull(employee);
    }

    @Test
    public void testGetEmployeeByNullUsername() {
        String error = "";
        Employee employee = null;
        try {
            employee = userService.getEmployeeByUsername(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(EMPTY_USERNAME, error);
        assertNull(employee);
    }

    @Test
    public void testUpdateEmployeePassword() {
        Employee employee = new Employee();
        employee.setUsername(EMPLOYEE_USERNAME);
        employee.setPassword(EMPLOYEE_PASSWORD);
        try {
           employee =  userService.updateEmployeePassword(employee, VISITOR_PASSWORD);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(VISITOR_PASSWORD, employee.getPassword());
        
    }

    @Test
    public void testUpdateEmployeeInvalidNewPassword() {
        String error = "";
        Employee employee = new Employee();
        employee.setUsername(EMPLOYEE_USERNAME);
        employee.setPassword(EMPLOYEE_PASSWORD);

        try {
            employee = userService.updateEmployeePassword(employee, BAD_PASSWORD);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(SHORT_PASSWORD, error);
        assertEquals(EMPLOYEE_PASSWORD, employee.getPassword());
    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee();
        employee.setUsername(EMPLOYEE_USERNAME);
        employee.setPassword(EMPLOYEE_PASSWORD);

        try {
            userService.deleteEmployee(employee);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testDeleteNullEmployee() {
        String error = "";
        try {
            userService.deleteEmployee(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(NULL_EMPLOYEE, error);
    }


    /* ------------------------------------------ Owner tests ------------------------------------------------*/

    @Test
    public void testUpdateOwnerPassword() {
        Owner owner = new Owner();
        owner.setUsername("admin1");
        owner.setPassword(OWNER_PASSWORD);

        try {
            owner = userService.updateOwnerPassword(owner, VISITOR_PASSWORD);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(VISITOR_PASSWORD, owner.getPassword());
    }

    @Test
    public void testUpdateOwnerInvalidNewPassword() {
        String error = "";
        Owner owner = new Owner();
        owner.setUsername("admin1");
        owner.setPassword(OWNER_PASSWORD);

        try {
            userService.updateOwnerPassword(owner, BAD_PASSWORD);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(SHORT_PASSWORD, error);
        assertEquals(OWNER_PASSWORD, owner.getPassword());
    }

    /* -------------------------------------------------Visitor tests ----------------------------------------------*/
    @Test
    public void testCreateVisitor() {
        assertEquals(0, userService.getAllVisitors().size());

        Visitor visitor = null;
        try {
            visitor = userService.createVisitor(UNIQUE_USERNAME, VISITOR_PASSWORD);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(visitor);
        assertEquals(UNIQUE_USERNAME, visitor.getUsername());
        assertEquals(VISITOR_PASSWORD, visitor.getPassword());
    }

    @Test
    public void testCreateVisitorInvalidUsername() {
        String error = "";
        Visitor visitor = null;
        try{
            visitor = userService.createVisitor("admin", VISITOR_PASSWORD );
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(INVALID_USERNAME, error);
        assertNull(visitor);
    }

    @Test
    public void testCreateVisitorUsernameTaken() {
        String error = "";
        Visitor visitor = null;
        try{
            visitor = userService.createVisitor(VISITOR_USERNAME, VISITOR_PASSWORD);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }            
        assertEquals(USERNAME_TAKEN, error);
        assertNull(visitor);
    }

    @Test
    public void testCreateVisitorShortPassword() {
        String error = "";
        Visitor visitor = null;
        try{
            visitor = userService.createVisitor(UNIQUE_USERNAME, BAD_PASSWORD);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }            
        assertEquals(SHORT_PASSWORD, error);
        assertNull(visitor);
    }

    @Test
    public void testGetExistingVisitorByUsername(){
        assertEquals(VISITOR_USERNAME2, userService.getVisitorByUsername(VISITOR_USERNAME2).getUsername());
    }

    @Test
    public void testGetNonExistingVisitorByUsername() {
        String error = "";
        Visitor visitor = null;
        try {
            visitor = userService.getVisitorByUsername(UNIQUE_USERNAME);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(NO_VISITOR, error);
        assertNull(visitor);
    }

    @Test
    public void testGetVisitorByNullUsername() {
        String error = "";
        Visitor visitor = null;
        try {
            visitor = userService.getVisitorByUsername(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(EMPTY_USERNAME, error);
        assertNull(visitor);
    }

    @Test
    public void testDeleteVisitor() {
        Visitor visitor = new Visitor();
        visitor.setUsername(VISITOR_USERNAME);
        visitor.setPassword(VISITOR_PASSWORD);

        try {
            userService.deleteVisitor(visitor);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testDeleteNullVisitor() {
        String error = "";
        try {
            userService.deleteVisitor(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(NO_VISITOR, error);
    }
}
