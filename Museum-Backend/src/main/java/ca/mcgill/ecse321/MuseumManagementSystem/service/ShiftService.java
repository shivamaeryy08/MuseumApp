package ca.mcgill.ecse321.MuseumManagementSystem.service;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.EmployeeRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Shift;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.ShiftRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShiftService {
    
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * @author Kara Best
     * @brief To create a new employee shift
     * @param employee
     * @param shiftdate
     * @param starttime
     * @param endtime
     * @return Shift - created shift
     */
    @Transactional
    public Shift createShift(Employee employee, Date shiftdate, Time starttime, Time endtime) {
        
        String err = "";
        Date currentDate = new Date(System.currentTimeMillis());
        if ( employee == null) {
            err += "Must specify employee! ";
        }
        if(shiftdate == null) {
            err += "Must specify shift date! ";
        }else if (shiftdate.before(currentDate)) {
            err += "Shift cannot be in the past! ";
        }
        if(starttime == null ) {
            err += "Must specify start time! ";
        }
        if(endtime == null ) {
            err += "Must specify end time!";
        } else if (starttime != null && endtime.before(starttime)) {
            err += "End time must be after start time!";
        }
        if(err.length()>0) {
            throw new IllegalArgumentException(err);
        }
        Shift shift = new Shift();
        shift.setEmployee(employee);
        shift.setEndTime(endtime);
        shift.setShiftDate(shiftdate);
        shift.setStartTime(starttime);
        return shiftRepository.save(shift);
    }
    
    /**
     * @author Kara Best
     * @brief To update the employee working the shift
     * @param shift
     * @param employee
     * @return Shift - updated shift
     */
    @Transactional
    public Shift updateShiftEmployee(Shift shift, Employee employee) {
        if(shift == null) {
            throw new IllegalArgumentException("Must select a shift!");
        }
        if(employee == null) {
            throw new IllegalArgumentException("Must specify employee!");
        }
        shift.setEmployee(employee);
        return shiftRepository.save(shift);
    }

    /**
     * @author Kara Best
     * @brief To delete a shift
     * @param shift
     */
    @Transactional
    public void deleteShift(Shift shift) {
        if(shift == null) {
            throw new IllegalArgumentException("Must select a shift!");
        }
        shiftRepository.delete(shift);
    }

    /**
     * @author Kara Best
     * @brief To get shifts of a specified employee on a specified date
     * @param employee
     * @param date
     * @return List<Shift>
     */
    @Transactional
    public List<Shift> getEmployeeShiftsOnDate(Employee employee, Date date) {
        if(employee == null || date == null) {
            throw new IllegalArgumentException("Must specify employee and date!");
        }
        return shiftRepository.findByEmployeeAndShiftDate(employee, date);
    }

    /**
     * @author Kara Best
     * @param employee
     * @return List<Shift>
     */
    @Transactional
    public List<Shift> getEmployeeShifts(Employee employee) {
        if(employee == null) {
            throw new IllegalArgumentException("Must specify employee!");
        }
        return shiftRepository.findByEmployee(employee);
    }

    /**
     * @author Kara Best
     * @brief To get shifts of a specified employee between two dates
     * @param employee
     * @param startdate
     * @param enddate
     * @return List<Shift>
     */
    @Transactional
    public List<Shift> getEmployeeShiftsBetweenDates(Employee employee, Date startdate, Date enddate) {
        if(employee == null || startdate == null || enddate == null) {
            throw new IllegalArgumentException("Must specify employee and dates!");
        }
        if(enddate.before(startdate) || enddate.equals(startdate)){
            throw new IllegalArgumentException("End date must be after start date!");
        }
        return shiftRepository.findByEmployeeAndShiftDateBetween(employee, startdate, enddate);
    }

    /**
     * @author Kara Best
     * @brief Get a shift by its ID
     * @param shiftID
     * @return Shift
     */
    @Transactional
    public Shift getShiftByID(Long shiftID) {
        Shift shift = shiftRepository.findShiftByShiftID(shiftID);
        if(shift == null) {
            throw new IllegalArgumentException("Shift does not exist!");
        }
        return shift;
    }

    /**
     * @author Kara Best
     * @brief Get all shifts
     * @return List<Shift>
     */
    @Transactional
    public List<Shift> getAllShifts() {
        return toList(shiftRepository.findAll());
    }

    // Helper method to retrieve lists of objects obtained from repository
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
