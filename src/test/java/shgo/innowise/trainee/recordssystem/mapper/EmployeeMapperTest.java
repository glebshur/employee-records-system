package shgo.innowise.trainee.recordssystem.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import shgo.innowise.trainee.recordssystem.dto.EmployeeDTO;
import shgo.innowise.trainee.recordssystem.entity.Employee;

public class EmployeeMapperTest {
    private EmployeeMapper mapper;

    @BeforeEach
    public void init(){
        mapper = Mappers.getMapper(EmployeeMapper.class);
    }

    @Test
    public void employeeToDTOShouldIgnorePassword(){
        Employee employee = new Employee();
        employee.setPassword("abc");
        employee.setFirstName("test");
        employee.setLastName("test");

        EmployeeDTO employeeDTO = mapper.employeeToDTO(employee);

        Assertions.assertNull(employeeDTO.getPassword());
    }
}
