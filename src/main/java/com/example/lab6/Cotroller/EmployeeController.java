package com.example.lab6.Cotroller;

import com.example.lab6.API.ApiResponse;
import com.example.lab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {


    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody @Valid Employee updateEmployee, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for(Employee employee : employees){
            if(employee.getId().equals(id)){
                employee.setName(updateEmployee.getName());
                employee.setEmail(updateEmployee.getEmail());
                employee.setPhoneNumber(updateEmployee.getPhoneNumber());
                employee.setAge(updateEmployee.getAge());
                employee.setPosition(updateEmployee.getPosition());
                employee.setHireDate(updateEmployee.getHireDate());
                employee.setAnnualLeave(updateEmployee.getAnnualLeave());

                return ResponseEntity.status(200).body(employee);

            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }

@DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id){
        for(Employee employee: employees){
            if(employee.getId().equals(id)){
                employees.remove(employee);
                return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/search/{position}")
    public ResponseEntity<?> searchByPosition( @PathVariable String position){

        if(!position.equalsIgnoreCase("supervisor") && !position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(400).body(new ApiResponse("Position must be either a supervisor or a coordinator"));
        }

        List<Employee> employeeList = new ArrayList<>();
        if(employeeList.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Employee found"));
        }

        //check if the array is empty or not

        for(Employee employee : employees){
            if(employee.getPosition().equalsIgnoreCase(position)){
                employeeList.add(employee);
            }
        }
        return ResponseEntity.status(200).body(employeeList);
    }

//*
    @GetMapping("/searchAge/{minAge}/{maxAge}")
    public ResponseEntity<?> searchByAge(@PathVariable int minAge, @PathVariable int maxAge){
        if(minAge<25 || maxAge <25 ){
            return ResponseEntity.status(400).body("Age must be more than 25");
        }
//
        List<Employee> employeeList = new ArrayList<>();
        if(employeeList.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Employee found"));
        }

        for(Employee employee: employees){
            if(employee.getAge()>=minAge && employee.getAge()<=maxAge){
                employeeList.add(employee);
            }
        }
        return ResponseEntity.status(200).body(employeeList);
    }

@PostMapping("/leave/{id}")
    public ResponseEntity<?> ApplyAnnulLeave(@PathVariable String id){
        for(Employee employee: employees){
            if(employee.getId().equals(id)) {
                if (employee.isOnLeave()) {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee is already on leave"));
                }
                if (employee.getAnnualLeave() < 1) {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee has no remaining leave "));
                }

                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave() - 1);
                return ResponseEntity.status(200).body(employee);
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }


    @GetMapping("/onleave")
    public ResponseEntity<?> getEmployeesWithNoLeave(){

        List<Employee> employeeList = new ArrayList<>();

        if(employeeList.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Employee found"));
        }
//
        for(Employee employee : employees){
            if(employee.isOnLeave()){
                employeeList.add(employee);
            }
        }
        return ResponseEntity.status(200).body(employeeList);

    }



    @PutMapping("/promote/{id}/{requestId}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String id, @PathVariable String requestId){

        Employee employeeToPromote = null;

        for(Employee employee: employees){
            if(employee.getId().equals(id)){
                employeeToPromote = employee;
                break;
            }
        }

        if(employeeToPromote == null){
            return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
        }

        Employee requesterEmp = null;

        for (Employee employee: employees){
            if(employee.getId().equals(requestId)){
                requesterEmp = employee;
                break;
            }
        }
        if(requesterEmp == null || !requesterEmp.getPosition().equalsIgnoreCase("supervisor")){
            return ResponseEntity.status(400).body(new ApiResponse("Requester must be a supervisor"));
        }

        if(employeeToPromote.getAge()<30){
            return ResponseEntity.status(400).body(new ApiResponse("To promote an employee, their age must be greater than 30"));
        }

        if(employeeToPromote.isOnLeave()){
            return ResponseEntity.status(400).body(new ApiResponse("Employee is on Leave and can't be promoted"));
        }

        employeeToPromote.setPosition("supervisor");

        return ResponseEntity.status(200).body(employeeToPromote);

    }

}
