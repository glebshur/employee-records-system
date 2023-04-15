package shgo.innowise.trainee.recordssystem.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shgo.innowise.trainee.recordssystem.dto.EmployeeDTO;
import shgo.innowise.trainee.recordssystem.entity.Employee;
import shgo.innowise.trainee.recordssystem.response.ResponseEntity;
import shgo.innowise.trainee.recordssystem.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private EmployeeService employeeService;
    private EmployeeController controller;
    private MockedStatic<EmployeeService> employeeServiceMockedStatic;

    @BeforeEach
    public void init() {
        employeeService = Mockito.mock(EmployeeService.class);
        employeeServiceMockedStatic = mockStatic(EmployeeService.class);
        employeeServiceMockedStatic.when(EmployeeService::getInstance).thenReturn(employeeService);
        controller = EmployeeController.getInstance();
    }

    @AfterEach
    public void clear() {
        employeeServiceMockedStatic.close();
    }


    @Test
    public void getEmployeeShouldThrowException() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/employee/get/abc");

        Assertions.assertThrows(NumberFormatException.class, () -> controller.getEmployee(request));
    }

    @Test
    public void getEmployeeShouldReturnEmployeeDto() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Employee employee = new Employee();
        employee.setId(11);
        employee.setFirstName("test");
        employee.setLastName("test");
        employee.setPassword("test1234");

        when(request.getPathInfo()).thenReturn("/employee/get/11");
        when(employeeService.getEmployee(11)).thenReturn(employee);

        ResponseEntity<EmployeeDTO> responseEntity = controller.getEmployee(request);

        Assertions.assertEquals("test", responseEntity.getBody().getFirstName());
        Assertions.assertEquals("test", responseEntity.getBody().getLastName());
        Assertions.assertEquals(11, responseEntity.getBody().getId());
        Assertions.assertNull(responseEntity.getBody().getPassword());
    }


    @Test
    public void deleteEmployeeShouldThrowException() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(request.getPathInfo()).thenReturn("/employee/delete/abc");

        Assertions.assertThrows(NumberFormatException.class, () -> controller.deleteEmployee(request));
    }

    @Test
    public void updateEmployeeShouldThrowException() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        when(request.getPathInfo()).thenReturn("/employee/update/abc");

        Assertions.assertThrows(NumberFormatException.class, () -> controller.updateEmployee(request));
    }


//    @Test
//    public void testTest() throws IOException, InterruptedException {
//        HttpClient httpClient = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .followRedirects(HttpClient.Redirect.NORMAL)
//                .build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://localhost:8080/employee/all"))
//                .GET()
//                .build();
//
//        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
//
//        Assertions.assertTrue(true);
//    }
}
