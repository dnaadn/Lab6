package com.example.lab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {


@NotEmpty(message = "Id is required")
@Size(min=2, message = "Id length must be more than 2")
    private String id;

@NotEmpty(message = "Name is required")
@Size(min = 4, message = "Name length must be more than 4")
@Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only characters")
    private String name;

@Email(message = "Email must be valid")
    private String email;


    @Pattern(regexp = "^05\\d{8}$", message = "Phone Number must start with 05 and be exactly 10 digits")

    private String phoneNumber;


@NotNull(message = "Age is required")
@Min(value = 25, message = "Age must be greater than 25")
    private Integer age;

@NotEmpty(message = "Position is required")
@Pattern(regexp = "supervisor|coordinator", message = "Position must be either a supervisor or a coordinator")
    private String position;

@AssertFalse
    private boolean onLeave;

@NotNull(message = " Hire Date can't be empty")
@PastOrPresent(message = "Hire Date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;


@NotNull(message = "Annual Leave can't be empty")
@Positive(message = "Leave days must be positive")
    private Integer AnnualLeave;
}
