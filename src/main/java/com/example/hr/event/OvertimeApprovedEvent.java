package com.example.hr.event;

public class OvertimeApprovedEvent extends HrEvent {
    private final Integer overtimeId;
    private final Integer employeeId;

    public OvertimeApprovedEvent(Object source, String actor, Integer overtimeId, Integer employeeId) {
        super(source, actor, "OVERTIME_APPROVED");
        this.overtimeId = overtimeId;
        this.employeeId = employeeId;
    }

    public Integer getOvertimeId() { return overtimeId; }
    public Integer getEmployeeId() { return employeeId; }
}
