package com.example.hr.event;

public class EmployeeHiredEvent extends HrEvent {
    private final Integer employeeId;
    private final String employeeName;

    public EmployeeHiredEvent(Object source, String actor, Integer employeeId, String employeeName) {
        super(source, actor, "EMPLOYEE_HIRED");
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public Integer getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
}
