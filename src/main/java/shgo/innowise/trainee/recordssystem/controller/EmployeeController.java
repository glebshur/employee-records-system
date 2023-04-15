package shgo.innowise.trainee.recordssystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import shgo.innowise.trainee.recordssystem.dto.EmployeeDTO;
import shgo.innowise.trainee.recordssystem.entity.Role;
import shgo.innowise.trainee.recordssystem.mapper.EmployeeMapper;
import shgo.innowise.trainee.recordssystem.response.MessageResponse;
import shgo.innowise.trainee.recordssystem.response.ResponseEntity;
import shgo.innowise.trainee.recordssystem.security.PermissionManager;
import shgo.innowise.trainee.recordssystem.service.EmployeeService;
import shgo.innowise.trainee.recordssystem.servlet.RoutingHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Employee controller.
 */
@Slf4j
public class EmployeeController {

    public static final String GET_ALL_EMPLOYEES_PATH = "/employee/all";
    public static final String GET_EMPLOYEE_PATH = "/employee/get/";
    public static final String CREATE_EMPLOYEE_PATH = "/employee/add";
    public static final String UPDATE_EMPLOYEE_PATH = "/employee/update/";
    public static final String DELETE_EMPLOYEE_PATH = "/employee/delete/";

    private EmployeeMapper employeeMapper;
    private EmployeeService employeeService;
    private ObjectMapper objectMapper;
    private static volatile EmployeeController instance;

    private EmployeeController() {
        employeeMapper = Mappers.getMapper(EmployeeMapper.class);
        employeeService = EmployeeService.getInstance();
        objectMapper = new ObjectMapper();

        // add roles permissions to paths
        PermissionManager permissionManager = PermissionManager.getInstance();
        permissionManager.addPathToRole(Role.USER, GET_ALL_EMPLOYEES_PATH);
        permissionManager.addPathToRole(Role.USER, GET_EMPLOYEE_PATH);

        permissionManager.addPathToRole(Role.ADMIN, GET_ALL_EMPLOYEES_PATH);
        permissionManager.addPathToRole(Role.ADMIN, GET_EMPLOYEE_PATH);
        permissionManager.addPathToRole(Role.ADMIN, CREATE_EMPLOYEE_PATH);
        permissionManager.addPathToRole(Role.ADMIN, UPDATE_EMPLOYEE_PATH);
        permissionManager.addPathToRole(Role.ADMIN, DELETE_EMPLOYEE_PATH);

        // add handlers to paths
        RoutingHelper helper = RoutingHelper.getInstance();
        helper.addGetPath(GET_EMPLOYEE_PATH, this::getEmployee);
        helper.addGetPath(GET_ALL_EMPLOYEES_PATH, this::getAllEmployees);
        helper.addPostPath(CREATE_EMPLOYEE_PATH, this::createEmployee);
        helper.addPutPath(UPDATE_EMPLOYEE_PATH, this::updateEmployee);
        helper.addDeletePath(DELETE_EMPLOYEE_PATH, this::deleteEmployee);
    }

    /**
     * Gives instance of EmployeeController object.
     *
     * @return instance of EmployeeController
     */
    public static EmployeeController getInstance() {
        EmployeeController localInstance = instance;
        if (localInstance == null) {
            synchronized (EmployeeController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = new EmployeeController();
                    localInstance = instance;
                }
            }
        }
        return localInstance;
    }

    /**
     * Gives employee by id.
     *
     * @param request  http request
     * @return employee dto response entity
     */
    public ResponseEntity<EmployeeDTO> getEmployee(HttpServletRequest request) {

        final long id = Long.parseLong(request.getPathInfo()
                .substring(GET_EMPLOYEE_PATH.length()));

        var objectToSend = employeeMapper.employeeToDTO(
                employeeService.getEmployee(id));

        return new ResponseEntity<>(objectToSend, HttpServletResponse.SC_OK);
    }

    /**
     * Gives all employees.
     *
     * @param request  http request
     * @return list of employees dto as response entity
     */
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(HttpServletRequest request) {

        List<EmployeeDTO> employees = employeeService.getAllEmployees().stream()
                .map(employee -> employeeMapper.employeeToDTO(employee))
                .collect(Collectors.toList());

        return new ResponseEntity<>(employees, HttpServletResponse.SC_OK);
    }

    /**
     * Creates employee.
     *
     * @param request  http request
     * @return employee dto response entity
     */
    public ResponseEntity<EmployeeDTO> createEmployee(HttpServletRequest request)
            throws IOException {

        EmployeeDTO employeeDTO = objectMapper.readValue(request.getInputStream(),
                EmployeeDTO.class);
        var objectToSend = employeeMapper.employeeToDTO(
                employeeService.createEmployee(employeeDTO));

        return new ResponseEntity<>(objectToSend, HttpServletResponse.SC_CREATED);
    }

    /**
     * Deletes employee.
     *
     * @param request  http request
     * @return message response as response entity
     */
    public ResponseEntity<MessageResponse> deleteEmployee(HttpServletRequest request) {

        final long id = Long.parseLong(request.getPathInfo()
                .substring(DELETE_EMPLOYEE_PATH.length()));
        employeeService.deleteEmployee(id);
        var objectToSend = new MessageResponse("Employee successfully deleted");

        return new ResponseEntity<>(objectToSend, HttpServletResponse.SC_OK);
    }

    /**
     * Updates employee.
     *
     * @param request  http request
     * @return employee dto response entity
     */
    public ResponseEntity<EmployeeDTO> updateEmployee(HttpServletRequest request)
            throws IOException {

        final long id = Long.parseLong(request.getPathInfo()
                .substring(UPDATE_EMPLOYEE_PATH.length()));
        EmployeeDTO employeeDTO = objectMapper.readValue(request.getInputStream(),
                EmployeeDTO.class);

        var objectToSend = employeeMapper.employeeToDTO(
                employeeService.updateEmployee(id, employeeDTO));

        return new ResponseEntity<>(objectToSend, HttpServletResponse.SC_OK);
    }
}
