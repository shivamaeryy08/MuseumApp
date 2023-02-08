package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
    Employee findEmployeeByUsername(String username);
}