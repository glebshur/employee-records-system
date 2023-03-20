package shgo.innowise.trainee.recordssystem.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.dao.EmployeeDAO;
import shgo.innowise.trainee.recordssystem.dto.EmployeeDTO;
import shgo.innowise.trainee.recordssystem.entity.Employee;
import shgo.innowise.trainee.recordssystem.entity.Role;
import shgo.innowise.trainee.recordssystem.exception.NotFoundException;
import shgo.innowise.trainee.recordssystem.security.PasswordEncoder;

/**
 * Implements employee's business logic.
 */
@Slf4j
public class EmployeeService {
    private EmployeeDAO employeeDAO;
    private PasswordEncoder passwordEncoder;
    private static EmployeeService instance;

    private EmployeeService() {
        employeeDAO = EmployeeDAO.getInstance();
        passwordEncoder = PasswordEncoder.getInstance();
    }

    /**
     * Gives instance of EmployeeService object.
     *
     * @return instance of EmployeeService
     */
    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }

        return instance;
    }

    /**
     * Gives employee by id.
     *
     * @param id employee's id
     * @return employee
     */
    public Employee getEmployee(final long id) {
        return employeeDAO.get(id).orElseThrow(() ->
                new NotFoundException("Employee cannot be found with id " + id));
    }

    /**
     * Gives all employees.
     *
     * @return employees list
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAll();
    }

    /**
     * Creates new employee.
     *
     * @param employeeDTO data to create
     * @return created employee
     */
    public Employee createEmployee(final EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setMiddleName(employeeDTO.getMiddleName());
        employee.setEmail(employeeDTO.getEmail());
        // password hashing
        employee.setPassword(passwordEncoder.getEncoder()
                .encode(employeeDTO.getPassword()));

        employee.setCreationDate(LocalDateTime.now());
        // add default role
        Set<Role> roles = employeeDTO.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(Role.USER);
        // prohibition on creating the admin
        roles.remove(Role.ADMIN);
        employee.setRoles(roles);

        log.info("Employee is creating with email " + employee.getEmail());
        return employeeDAO.create(employee);
    }

    /**
     * Updates employee.
     *
     * @param employeeDTO data to update
     * @return updated employee
     */
    public Employee updateEmployee(final long employeeId,
                                   final EmployeeDTO employeeDTO) {
        Employee employeeToUpdate = employeeDAO.get(employeeId).orElseThrow(() ->
                new NotFoundException("Employee cannot be found with id "
                        + employeeDTO.getId()));

        employeeToUpdate.setFirstName(employeeDTO.getFirstName());
        employeeToUpdate.setLastName(employeeDTO.getLastName());
        employeeToUpdate.setMiddleName(employeeDTO.getMiddleName());

        // add default role
        Set<Role> newRoles = employeeDTO.getRoles();
        newRoles.add(Role.USER);
        // prohibition on changing the admin role
        if (employeeToUpdate.getRoles().contains(Role.ADMIN)) {
            newRoles.add(Role.ADMIN);
        } else {
            newRoles.remove(Role.ADMIN);
        }
        employeeToUpdate.setRoles(newRoles);

        log.info("Updating employee with id " + employeeToUpdate.getId());
        return employeeDAO.update(employeeToUpdate);
    }

    /**
     * Deletes employee.
     *
     * @param id id of employee to delete
     */
    public void deleteEmployee(final long id) {
        Employee employeeToDelete = employeeDAO.get(id).orElseThrow(() ->
                new NotFoundException("Employee cannot be found with id " + id));
        log.info("Deleting employee with id " + id);
        employeeDAO.delete(employeeToDelete);
    }
}
