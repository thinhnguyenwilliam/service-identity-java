package com.example.dev.mapper;

import com.example.dev.dto.request.DepartmentDTO;
import com.example.dev.dto.request.EmployeeDTO;
import com.example.dev.entity.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDTO toDTO(Department department);
    Department toEntity(DepartmentDTO departmentDTO);

    EmployeeDTO toDTO(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);

    List<EmployeeDTO> toEmployeeDTOList(List<Employee> employees);
    List<Employee> toEmployeeList(List<EmployeeDTO> employeeDTOs);
}


