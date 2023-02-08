package ca.mcgill.ecse321.MuseumManagementSystem.dto;

import java.sql.Date;
import java.sql.Time;

public class ShiftDto {
    private Long shiftID;
    private Date shiftDate;
    private Time startTime;
    private Time endTime;
    private EmployeeDto employee;

    public ShiftDto() {}

    //Constructor with shiftID param
    public ShiftDto(Long shiftID, Date shiftDate, Time startTime, Time endTime, EmployeeDto employee) {
        this.shiftID = shiftID;
        this.shiftDate = shiftDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }

    //Constructor without shiftID param
    public ShiftDto(Date shiftDate, Time startTime, Time endTime, EmployeeDto employee) {
        this.shiftDate = shiftDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }

    public Date getShiftDate()
    {
      return shiftDate;
    }
  
    public Time getStartTime()
    {
      return startTime;
    }
  
    public Time getEndTime()
    {
      return endTime;
    }
  
    public Long getShiftID()
    {
      return shiftID;
    }

    public EmployeeDto getEmployee()
    {
      return employee;
    }

    public void setEmployee(EmployeeDto employeeDto) {
      this.employee = employeeDto;
    }
}
