package shgo.innowise.trainee.recordssystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shgo.innowise.trainee.recordssystem.dto.EmployeeDTO;
import shgo.innowise.trainee.recordssystem.entity.Employee;

/**
 * Employee mapper.
 */
@Mapper
public interface EmployeeMapper {

    @Mapping(target = "password", ignore = true)
    EmployeeDTO employeeToDTO(Employee employee);

    @Mapping(target = "creationDate", ignore = true)
    Employee dtoToEmployee(EmployeeDTO employeeDTO);
}
