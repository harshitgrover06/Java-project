package com.example.datastructure.dto;

import com.example.datastructure.model.Employee;

public class EmployeeDTOConverter {

    public static EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDateOfBirth(employee.getDateOfBirth().toString()); // Convert date to string
        dto.setDateOfJoining(employee.getDateOfJoining().toString()); // Convert date to string
        dto.setGrade(String.valueOf(employee.getGrade()));
        return dto;
    }
}
