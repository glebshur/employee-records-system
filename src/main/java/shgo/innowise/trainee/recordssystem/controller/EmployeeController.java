package shgo.innowise.trainee.recordssystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import shgo.innowise.trainee.recordssystem.dto.EmployeeDTO;
import shgo.innowise.trainee.recordssystem.mapper.EmployeeMapper;
import shgo.innowise.trainee.recordssystem.response.MessageResponse;
import shgo.innowise.trainee.recordssystem.service.EmployeeService;
import shgo.innowise.trainee.recordssystem.util.ControllerUtil;
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
    private static EmployeeController instance;

    private EmployeeController() {
        employeeMapper = Mappers.getMapper(EmployeeMapper.class);
        employeeService = EmployeeService.getInstance();
        objectMapper = new ObjectMapper();
    }

    /**
     * Gives instance of EmployeeController object.
     *
     * @return instance of EmployeeController
     */
    public static EmployeeController getInstance() {
        if (instance == null) {
            instance = new EmployeeController();
        }
        return instance;
    }

    /**
     * Gives employee by id.
     *
     * @param request  http request
     * @param response http response
     */
    public void getEmployee(HttpServletRequest request, HttpServletResponse response) {

        final long id = Long.parseLong(request.getPathInfo()
                .substring(GET_EMPLOYEE_PATH.length()));

        var objectToSend = employeeMapper.employeeToDTO(
                employeeService.getEmployee(id));

        ControllerUtil.fillJsonResponse(response, HttpServletResponse.SC_OK,
                objectMapper, objectToSend);
    }

    /**
     * Gives all employees.
     *
     * @param request  http request
     * @param response http response
     */
    public void getAllEmployees(HttpServletRequest request, HttpServletResponse response) {

        List<EmployeeDTO> employees = employeeService.getAllEmployees().stream()
                .map(employee -> employeeMapper.employeeToDTO(employee))
                .collect(Collectors.toList());

        ControllerUtil.fillJsonResponse(response, HttpServletResponse.SC_OK,
                objectMapper, employees);
    }

    /**
     * Creates employee.
     *
     * @param request  http request
     * @param response http response
     */
    public void createEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        EmployeeDTO employeeDTO = objectMapper.readValue(request.getInputStream(),
                EmployeeDTO.class);
        var objectToSend = employeeMapper.employeeToDTO(
                employeeService.createEmployee(employeeDTO));

        ControllerUtil.fillJsonResponse(response, HttpServletResponse.SC_CREATED,
                objectMapper, objectToSend);
    }

    /**
     * Deletes employee.
     *
     * @param request  http request
     * @param response http response
     */
    public void deleteEmployee(HttpServletRequest request, HttpServletResponse response) {

        final long id = Long.parseLong(request.getPathInfo()
                .substring(DELETE_EMPLOYEE_PATH.length()));
        employeeService.deleteEmployee(id);
        var objectToSend = new MessageResponse("Employee successfully deleted");

        ControllerUtil.fillJsonResponse(response, HttpServletResponse.SC_OK,
                objectMapper, objectToSend);
    }

    /**
     * Updates employee.
     *
     * @param request  http request
     * @param response http response
     */
    public void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final long id = Long.parseLong(request.getPathInfo()
                .substring(UPDATE_EMPLOYEE_PATH.length()));
        EmployeeDTO employeeDTO = objectMapper.readValue(request.getInputStream(),
                EmployeeDTO.class);

        var objectToSend = employeeMapper.employeeToDTO(
                employeeService.updateEmployee(id, employeeDTO));

        ControllerUtil.fillJsonResponse(response, HttpServletResponse.SC_OK,
                objectMapper, objectToSend);
    }
}
