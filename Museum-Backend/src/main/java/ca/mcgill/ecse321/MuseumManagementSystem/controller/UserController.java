package ca.mcgill.ecse321.MuseumManagementSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MuseumManagementSystem.dto.EmployeeDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.OwnerDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.VisitorDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * @author Kara Best
     * @param accounttype
     * @param username
     * @param password
     * @return ResponseEntity<String>
     */
    @GetMapping(value = {"/login/{accounttype}/{username}/{password}", "/login/{accounttype}/{username}/{password}/"})
    public ResponseEntity<String> userLogIn(@PathVariable(name = "accounttype") String accounttype, @PathVariable(name = "username") String username, @PathVariable(name="password") String password) {
        try {
            boolean authenticated = false;
            if(accounttype.contains("A")) {
                if(username.equals("admin")){
                    Owner owner = userService.getOwner();
                    if(owner.getPassword().equals(password)) authenticated = true;                
                }
            } else if(accounttype.contains("E")) {
                if(username.startsWith("mms_")) {
                    Employee employee = userService.getEmployeeByUsername(username);
                    if(employee.getPassword().equals(password)) authenticated = true;
                }
            } else {
                Visitor visitor = userService.getVisitorByUsername(username);
                if(visitor.getPassword().equals(password)) authenticated = true;
            }
            if(authenticated) {
                return new ResponseEntity<>("User authenticated", HttpStatus.OK);
            }
            return new ResponseEntity<>("Username or/and password is wrong!", HttpStatus.BAD_REQUEST);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>("Username or/and password is wrong!", HttpStatus.BAD_REQUEST);
        }
    }    
    /* ------------------------------------------ Visitor Controller Methods --------------------------------------------------- */

    /** 
     * @author Kara Best
     * @param username
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/visitor/{username}", "/visitor/{username}/"})
    public ResponseEntity<?> getVisitorByUsername(@PathVariable String username) {
        try 
        {
            Visitor visitor = userService.getVisitorByUsername(username);
            VisitorDto visitorDto = convertToDto(visitor);
            return new ResponseEntity<>(visitorDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    
    /** 
     * @author Kara Best
     * @param request
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/visitor", "/visitor/"})
    public ResponseEntity<?> createVisitor(@RequestBody VisitorDto request) { //request received from person using website (does not contain id, that field is empty for Dto object)
        try 
        {
            Visitor visitor = userService.createVisitor(request.getUsername(), request.getPassword()); //create instance of visitor using service method (creates autogened id)
            VisitorDto visitorDto = convertToDto(visitor);
            return new ResponseEntity<>(visitorDto, HttpStatus.CREATED); //returns response object + http status to website
        } 
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    
    /** 
     * @author Kara Best
     * @param username
     */
    @DeleteMapping(value = {"/visitor/{username}", "/visitor/{username}/"}) 
    public void deleteVisitor(@PathVariable("username") String username) {
        Visitor visitor = userService.getVisitorByUsername(username);
        userService.deleteVisitor(visitor);   
    }

    /* ------------------------------------------ Employee Controller Methods --------------------------------------------------- */

    /** 
     * @author Kara Best
     * @param username
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/employee/{username}", "/employee/{username}/"})
    public ResponseEntity<?> getEmployeeByUsername(@PathVariable String username) {
        try 
        {
            Employee employee = userService.getEmployeeByUsername(username);
            EmployeeDto employeeDto = convertToDto(employee);
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    /**
     * @author Kara Best
     * @return ResponseEntity<?>
     */
    @GetMapping(value={"/employees", "/employees/"})
    public ResponseEntity<?> getAllEmployees() {
        return new ResponseEntity<>(userService.getAllEmployees(), HttpStatus.OK);
    }

    
    /** 
     * @author Kara Best
     * @param request
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/employee", "/employee/"}) //might need to change this to admin/employee or something
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto request) { 
        try 
        {
            Employee employee = userService.createEmployee(request.getUsername(), request.getPassword()); 
            EmployeeDto employeeDto = convertToDto(employee);
            return new ResponseEntity<>(employeeDto, HttpStatus.CREATED); 
        } 
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    
    /**
     * @author Kara Best 
     * @param username
     * @param request
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/employee/{username}/updatePassword", "/employee/{username}/updatePassword/"})
    public ResponseEntity<?> updateEmployeePassword(@PathVariable("username") String username, @RequestBody EmployeeDto request) {
        //Retrieve non-updated employee
        try
        {
            Employee employee = userService.getEmployeeByUsername(username);
            //update employee email
            employee = userService.updateEmployeePassword(employee, request.getPassword());
            EmployeeDto employeeDto = convertToDto(employee);
            //return updated employee
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            //return non-updated employee
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    
    /**
     * @author Kara Best
     * @param username
     */
    @DeleteMapping(value = {"/employee/{username}", "/employee/{username}/"})
    public void deleteEmployee(@PathVariable("username") String username ) {

        Employee employee = userService.getEmployeeByUsername(username);
        userService.deleteEmployee(employee);   

    }

    /* ------------------------------------------ Owner Controller Methods --------------------------------------------------- */

    /** 
     * @author Kara Best
     * @param request
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/admin", "/admin/"})
    public ResponseEntity<?> getOwner() {
        try 
        {
            Owner owner = userService.getOwner();
            OwnerDto ownerDto = convertToDto(owner);
            return new ResponseEntity<>(ownerDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    
    /** 
     * @author Kara Best
     * @param vDto
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/admin/updatePassword", "/admin/updatePassword/"})
    public ResponseEntity<?> updateOwnerPassword(@RequestBody OwnerDto vDto) {
        
        try
        {   //Retrieve non-updated owner
            Owner owner = userService.getOwner();
            //update owner email
            owner = userService.updateOwnerPassword(owner, vDto.getPassword());
            OwnerDto ownerDto = convertToDto(owner);
            //return updated owner
            return new ResponseEntity<>(ownerDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            //return non-updated owner
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* ------------------------------------------- Helper Methods --------------------------------------- */

    private VisitorDto convertToDto(Visitor visitor) {
        
        if(visitor == null) {
            return null;
        }
        return new VisitorDto(visitor.getUsername(), visitor.getPassword());
    }

    private EmployeeDto convertToDto(Employee employee) {
        
        if(employee == null) {
            return null;
        }
        return new EmployeeDto(employee.getUsername(), employee.getPassword());
    }

    private OwnerDto convertToDto(Owner owner) {
        
        if(owner == null) {
            return null;
        }
        return new OwnerDto(owner.getUsername(), owner.getPassword());
    }
}
