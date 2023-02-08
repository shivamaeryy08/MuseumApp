package ca.mcgill.ecse321.MuseumManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.EmployeeRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.ShiftRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Shift;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;


@ExtendWith(MockitoExtension.class)
public class TestShiftService {

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ShiftService shiftService;

    private static final Long SHIFT_ID = 100L;
    private static final Date SHIFT_DATE = Date.valueOf("2025-12-05");
    private static final Time START_TIME = Time.valueOf("10:00:00");
    private static final Time END_TIME = Time.valueOf("14:00:00");
    private static final Employee EMPLOYEE = new Employee();
    private static final Shift EMPTY_SHIFT = new Shift();

    private static final Date PAST_SHIFT_DATE = Date.valueOf("2020-08-08");

    private static final String NULL_EMPLOYEE = "Must specify employee! ";
    private static final String NULL_DATE = "Must specify shift date! ";
    private static final String NULL_SHIFT = "Must select a shift!";
    private static final String END_BEFORE_START = "End time must be after start time!";
    private static final String NULL_START_TIME = "Must specify start time! ";
    private static final String NULL_END_TIME = "Must specify end time!";
    private static final String DATE_PASSED = "Shift cannot be in the past! ";

    @BeforeEach
    public void setMockOutput() {

        lenient().when(shiftRepository.findShiftByShiftID(anyLong())).thenAnswer(
            (InvocationOnMock invocation) -> {
                if(invocation.getArgument(0).equals(SHIFT_ID)) {
                    Shift shift = new Shift();
                    shift.setShiftID(SHIFT_ID);
                    return shift;
                } else {
                    return null;
                }
            }
        );
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
        Answer<?> returnEmptyList = (InvocationOnMock invocation) -> {
			return new ArrayList<Shift>();
		};
        lenient().when(shiftRepository.save(any(Shift.class))).thenAnswer(returnParameterAsAnswer);
        lenient().doNothing().when(shiftRepository).delete(any(Shift.class));
        lenient().when(shiftRepository.findByShiftDate(any(Date.class))).thenAnswer(returnEmptyList);
        lenient().when(shiftRepository.findByShiftDateBetween(any(Date.class), any(Date.class))).thenAnswer(returnEmptyList);
        lenient().when(shiftRepository.findByEmployee(any(Employee.class))).thenAnswer(returnEmptyList);
        lenient().when(shiftRepository.findByEmployeeAndShiftDate(any(Employee.class), any(Date.class))).thenAnswer(returnEmptyList);
        lenient().when(shiftRepository.findByEmployeeAndShiftDateBetween(any(Employee.class), any(Date.class), any(Date.class))).thenAnswer(returnEmptyList);
    }

    @Test
    public void testCreateShift() {
        Shift shift = null;
        try {
            shift = shiftService.createShift(EMPLOYEE, SHIFT_DATE, START_TIME, END_TIME);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(shift);
        assertEquals(SHIFT_DATE.toString(), shift.getShiftDate().toString());
        assertEquals(EMPLOYEE, shift.getEmployee());
        assertEquals(START_TIME.toString(), shift.getStartTime().toString());
        assertEquals(END_TIME.toString(), shift.getEndTime().toString());
    }

    @Test
    public void testCreateShiftNullParameters() {
        String error = "";
        try {
            shiftService.createShift(null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
            
        }
        assertEquals(NULL_EMPLOYEE+NULL_DATE+NULL_START_TIME+NULL_END_TIME, error);
    }

    @Test
    public void testCreateShiftPassedDate() {
        String error = "";
        try {
            shiftService.createShift(EMPLOYEE, PAST_SHIFT_DATE, START_TIME, END_TIME);
        }catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(DATE_PASSED, error);
    }

    @Test
    public void testCreateShiftEndTimeBeforeStartTime() {
        String error = "";
        try {
            shiftService.createShift(EMPLOYEE, SHIFT_DATE, END_TIME, START_TIME);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
            
        }
        assertEquals(END_BEFORE_START, error);
    }

    @Test
    public void testUpdateShiftEmployee() {
        Shift shift = new Shift();
        try {
            shift = shiftService.updateShiftEmployee(shift, EMPLOYEE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(EMPLOYEE, shift.getEmployee());
    }

    @Test
    public void testUpdateShiftEmployeeNullShift() {
        String error = "";
        try {
            shiftService.updateShiftEmployee(null, EMPLOYEE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();          
        }
        assertEquals(NULL_SHIFT, error);
    }
    
    @Test
    public void testUpdateShiftEmployeeNull() {
        String error = "";
        try {
            shiftService.updateShiftEmployee(EMPTY_SHIFT, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(NULL_EMPLOYEE.trim(), error);
    }

    @Test
    public void testDeleteShift() {
        try {
            shiftService.deleteShift(EMPTY_SHIFT);
        }catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testDeleteShiftNull() {
        String error = "";
        try {
            shiftService.deleteShift(null);
        }catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(NULL_SHIFT, error);
    }

    @Test
    public void testGetEmployeeShiftsOnDate() {
        List<Shift> shifts = null;
        try {
            shifts = shiftService.getEmployeeShiftsOnDate(EMPLOYEE, SHIFT_DATE);
        }catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(shifts);
    }

    @Test
    public void testGetEmployeeShiftsOnDateNullParameters() {
        List<Shift> shifts = null;
        String error = "";
        try {
            shifts = shiftService.getEmployeeShiftsOnDate(null, null);
        }catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Must specify employee and date!", error);
        assertNull(shifts);
    }

    @Test
    public void testGetEmployeeShiftsBetweenDates() {
        List<Shift> shifts = null;
        try {
            shifts = shiftService.getEmployeeShiftsBetweenDates(EMPLOYEE, PAST_SHIFT_DATE, SHIFT_DATE);
        }catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(shifts);
    }

    @Test
    public void testGetEmployeeShiftsBetweenDatesNullParameters() {
        List<Shift> shifts = null;
        String error = "";
        try {
            shifts = shiftService.getEmployeeShiftsBetweenDates(null, null, null);
        }catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Must specify employee and dates!", error);
        assertNull(shifts);
    }

    @Test
    public void testGetEmployeeShiftsBetweenDatesEndAfterStart() {
        List<Shift> shifts = null;
        String error = "";
        try {
            shifts = shiftService.getEmployeeShiftsBetweenDates(EMPLOYEE, SHIFT_DATE, PAST_SHIFT_DATE);
        }catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("End date must be after start date!", error);
        assertNull(shifts);
    }

    @Test
    public void testGetShiftByID() {
        Shift shift = null;
        try{
            shift = shiftService.getShiftByID(SHIFT_ID);
        }catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(shift);
        assertEquals(SHIFT_ID, shift.getShiftID());
    }
    @Test
    public void testGetNonExistantShiftByID() {
        Shift shift = null;
        String error = "";
        try{
            shift = shiftService.getShiftByID(1L);
        }catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Shift does not exist!", error);
        assertNull(shift);
    }
}
