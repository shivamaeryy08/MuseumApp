package ca.mcgill.ecse321.MuseumManagementSystem.service;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.EmployeeRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OrderInformationRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.VisitorRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.OwnerRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.ShiftRepository;

@Service
public class UserService {
    
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    VisitorRepository visitorRepository;
    @Autowired
    LoanRequestRepository loanRequestRepository;
    @Autowired
    OrderInformationRepository orderInformationRepository;
    @Autowired
    ShiftRepository shiftRepository;

    /* ------------------------- Owner Methods --------------------------- */

    /**
     * @author Kara Best
     * @brief To update owner password
     * @param owner
     * @param oldPassword
     * @param newPassword
     * @return Owner - Updated owner object
     */
    @Transactional
    public Owner updateOwnerPassword(Owner owner, String newPassword) {
        String err = "";
        if(owner == null) {
            throw new IllegalArgumentException("Must specify owner!");
        }
        if(newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Must enter new password!");
        }
        if (newPassword.length() < 7) {
            err += "Password must contain 7 or more characters!";
        }
        if(!err.isEmpty()) {
            throw new IllegalArgumentException(err);
        }

        owner.setPassword(newPassword);
        return ownerRepository.save(owner);
    }
    
    /**
     * @author Kara Best
     * @brief To get owner account
     * @return Owner
     */
    @Transactional
    public Owner getOwner() {
        return ownerRepository.findOwnerByUsername("admin");
    }

    @Transactional
    public List<Owner> getAllOwners() {
        return toList(ownerRepository.findAll());
    }

    /* ---------------------------------------------Employee Methods -----------------------------------------------*/
    
    /**
     * @author Kara Best
     * 
     * @brief To create an employee account
     * 
     * @param username
     * @param email
     * @param password
     * @return Employee - Created employee instance
     */
    @Transactional
    public Employee createEmployee(String username, String password) {
        
        /* Employee usernames preceded by "mms_" */
        String prefix = "mms_";

        /* Input validition */
        String err = "";
        if( username == null || username.isBlank() ) {
            err += "Invalid username! ";
        }
        else if(employeeRepository.findEmployeeByUsername(prefix+username) != null) {
            err += "Username is taken! ";
        }
        
        if (password == null || password.length() < 7) {
            err += "Password must contain 7 or more characters!";
        }

        if( !err.isEmpty() ) {
            throw new IllegalArgumentException(err);
        }

        Employee employee = new Employee();
        employee.setUsername(prefix+username);
        employee.setPassword(password);
        return employeeRepository.save(employee);
    }

    /**
     * @author Kara Best
     * @brief To update employee password
     * @param employee
     * @param oldPassword
     * @param newPassword
     * @return Employee - Updated employee object
     */
    @Transactional
    public Employee updateEmployeePassword(Employee employee, String newPassword) {

        if( employee == null ) {
            throw new IllegalArgumentException("Must specify employee!");
        }
        String err = "";
        //Check for either password null or blank
        if(newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Must enter new password!");
        }
        if (newPassword.length() < 7) {
            err += "Password must contain 7 or more characters!";
        }
        if( !err.isEmpty() ) {
            throw new IllegalArgumentException(err);
        }
        employee.setPassword(newPassword);
        return employeeRepository.save(employee);
    }

    /**
     * @author Kara Best
     * @brief Gets Employee by username
     * @param username
     * @return Employee
     */
    @Transactional
    public Employee getEmployeeByUsername(String username) {
        if(username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        Employee employee = employeeRepository.findEmployeeByUsername(username);
        if(employee == null) {
            throw new IllegalArgumentException("Employee does not exist!");
        }
        return employee;
    }

    /**
     * @author Kara Best
     * @brief gets full list of employees
     * @return List<Employee>
     */
    @Transactional
    public List<Employee> getAllEmployees() {
        return toList(employeeRepository.findAll());
    }

    /**
     * @author Kara Best
     * @brief deletes specified employee and their shifts
     * @param employee
     */
    @Transactional
    public void deleteEmployee(Employee employee) {

        if( employee == null ) {
            throw new IllegalArgumentException("Must specify employee!");
        }
        /* Delete employee shifts */
        shiftRepository.deleteAllByEmployee(employee);
        employeeRepository.delete(employee);
    }


    /* --------------------------------------- Visitor Methods ----------------------------------------*/
    
    /**
     * @author Kara Best
     * @brief to create a new visitor account
     * @param username
     * @param email
     * @param password
     * @return Visitor - new visitor account
     */
    @Transactional
    public Visitor createVisitor(String username, String password) {
        
        /* Input validition */
        String err = "";
        if( username == null || username.isBlank() || username.startsWith("mms_") || username.equals("admin")) {
            err += "Invalid username! ";
        }
        else if(visitorRepository.findVisitorByUsername(username) != null) {
            err += "Username is taken! ";
        }

        if (password == null || password.length() < 7) {
            err += "Password must contain 7 or more characters!";
        }
        if( !err.isEmpty() ) {
            throw new IllegalArgumentException(err);
        }

        Visitor visitor = new Visitor();
        visitor.setUsername(username);
        visitor.setPassword(password);
        return visitorRepository.save(visitor);
    }

    /**
     * @author Kara Best
     * @brief To delete a visitor account
     * @param visitor
     */
    @Transactional
    public void deleteVisitor(Visitor visitor) 
    {
       if( visitor == null ) {
            throw new IllegalArgumentException("Visitor does not exist!");
        }
        /* Delete all visitor dependencies (order information and loan requests) */
        loanRequestRepository.deleteAllByRequester(visitor);
        orderInformationRepository.deleteAllByVisitor(visitor);

        visitorRepository.delete(visitor);
    }

    /**
     * @author Kara Best
     * @brief To get a visitor account by username
     * @param username
     * @return Visitor
     */
    @Transactional
    public Visitor getVisitorByUsername(String username) {
        if(username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        Visitor visitor = visitorRepository.findVisitorByUsername(username);
        if( visitor == null ) {
            throw new IllegalArgumentException("Visitor does not exist!");
        }
        return visitor;
    }

    /**
     * @author Kara Best
     * @brief To get the full list of registered visitors
     * @return List<Visitor>
     */
    @Transactional
    public List<Visitor> getAllVisitors() {
        return toList(visitorRepository.findAll());
    }

    /* Helper Methods */
    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
