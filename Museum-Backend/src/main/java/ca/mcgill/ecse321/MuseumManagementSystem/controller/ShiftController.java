package ca.mcgill.ecse321.MuseumManagementSystem.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MuseumManagementSystem.dto.EmployeeDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.ShiftDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Shift;
import ca.mcgill.ecse321.MuseumManagementSystem.service.ShiftService;
import ca.mcgill.ecse321.MuseumManagementSystem.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class ShiftController {

    @Autowired
    ShiftService shiftService;

    @Autowired
    UserService userService;

    
    /** 
     * @author Kara Best
     * @param shiftID
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/shift/{shiftID}", "/shift/{shiftID}/"})
    public ResponseEntity<?> getShiftByShiftID(@PathVariable Long shiftID) {
        try {
            Shift shift = shiftService.getShiftByID(shiftID);
            ShiftDto shiftDto = convertToDto(shift);
            return new ResponseEntity<>(shiftDto, HttpStatus.OK); 
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
    @GetMapping(value = {"/shifts", "/shifts/"})
    public ResponseEntity<?> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return new ResponseEntity<>(convertToDto(shifts), HttpStatus.OK);
    }

    
    /** 
     * @author Kara Best
     * @param username
     * @param date
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/shifts/{username}/{date}", "/shifts/{username}/{date}/"})
    public ResponseEntity<?> getEmployeeShiftsOnDate(@PathVariable("username") String username, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            Employee employee = userService.getEmployeeByUsername(username);
            List<Shift> shifts = shiftService.getEmployeeShiftsOnDate(employee, Date.valueOf(date));
            return new ResponseEntity<>(convertToDto(shifts), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @author Kara Best
     * @param username
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/shifts/{username}","/shifts/{username}/"})
    public ResponseEntity<?> getEmployeeShifts(@PathVariable("username") String username) {
        try {
            Employee employee = userService.getEmployeeByUsername(username);
            List<Shift> shifts = shiftService.getEmployeeShifts(employee);
            return new ResponseEntity<>(convertToDto(shifts), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    
    /** 
     * @author Kara Best
     * @param username
     * @param date1
     * @param date2
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/shifts/{username}/{date1}/{date2}", "/shifts/{username}/{date1}/{date2}/"})
    public ResponseEntity<?> getEmployeeShiftsBetweenDates(@PathVariable(name = "username") String username, @PathVariable(name = "date1") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1, @PathVariable(name = "date2") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2) {
        try {
            Employee employee = userService.getEmployeeByUsername(username);
            List<Shift> shifts = shiftService.getEmployeeShiftsBetweenDates(employee, Date.valueOf(date1), Date.valueOf(date2));
            return new ResponseEntity<>(convertToDto(shifts), HttpStatus.OK);
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
    @PostMapping(value = {"/shift", "/shift/"})
    public ResponseEntity<?> createShift(@RequestBody ShiftDto request) {
        try {
            Employee employee = userService.getEmployeeByUsername(request.getEmployee().getUsername());
            Shift shift = shiftService.createShift(employee, request.getShiftDate(), request.getStartTime(), request.getEndTime());
            ShiftDto shiftDto = convertToDto(shift);
            return new ResponseEntity<>(shiftDto, HttpStatus.CREATED); 
        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    
    /** 
     * @author Kara Best
     * @param shiftID
     */
    @DeleteMapping(value = {"/shift/{shiftID}", "/shift/{shiftID}/"})
    public void deleteShift(@PathVariable Long shiftID) {
        Shift shift = shiftService.getShiftByID(shiftID);
        shiftService.deleteShift(shift);
    }

    
    /** 
     * @author Kara Best
     * @param shiftID
     * @param username
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/shift/{shiftID}/updateEmployee/{username}", "/shift/{shiftID}/updateEmployee/{username}/"})
    public ResponseEntity<?> updateShiftEmployee(@PathVariable(name = "shiftID") Long shiftID, @PathVariable(name = "username") String username) {
        Shift shift = null;
        try {
            shift = shiftService.getShiftByID(shiftID);
            Employee employee = userService.getEmployeeByUsername(username);
            shift = shiftService.updateShiftEmployee(shift, employee);
            return new ResponseEntity<>(convertToDto(shift), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* --------------------------------------- Helper Methods ---------------------------------- */
    
    private ShiftDto convertToDto(Shift shift) {
        if (shift == null) {
            return null;
        }
        return new ShiftDto(shift.getShiftID(), shift.getShiftDate(), shift.getStartTime(), shift.getEndTime(), convertToDto(shift.getEmployee()));
    }

    private ArrayList<ShiftDto> convertToDto(List<Shift> shifts) {
        ArrayList<ShiftDto> shiftsDto = new ArrayList<ShiftDto>();
        for (Shift shift : shifts) {
            shiftsDto.add(convertToDto(shift));
        }
        return shiftsDto;
    }

    private EmployeeDto convertToDto(Employee employee) {
        if (employee == null ) {
            return null;
        }
        return new EmployeeDto(employee.getUsername(), employee.getPassword());
    }
    
}
