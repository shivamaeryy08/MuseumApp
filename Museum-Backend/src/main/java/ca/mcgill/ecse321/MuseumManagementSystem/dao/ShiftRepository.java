package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Shift;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;

public interface ShiftRepository extends CrudRepository<Shift, Long> {
    Shift findShiftByShiftID(Long shiftID);
    List<Shift> findByEmployee(Employee employee);
    void deleteAllByEmployee(Employee employee);
    List<Shift> findByShiftDate(Date shiftDate);
    List<Shift> findByShiftDateBetween(Date startDate, Date endDate);
    List<Shift> findByEmployeeAndShiftDateBetween(Employee employee, Date startdate, Date enddate);
    List<Shift> findByEmployeeAndShiftDate(Employee employee, Date shiftDate);
}