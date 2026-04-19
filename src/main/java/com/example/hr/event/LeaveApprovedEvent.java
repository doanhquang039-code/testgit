package com.example.hr.event;

public class LeaveApprovedEvent extends HrEvent {
    private final Integer leaveId;
    private final Integer employeeId;

    public LeaveApprovedEvent(Object source, String actor, Integer leaveId, Integer employeeId) {
        super(source, actor, "LEAVE_APPROVED");
        this.leaveId = leaveId;
        this.employeeId = employeeId;
    }

    public Integer getLeaveId() { return leaveId; }
    public Integer getEmployeeId() { return employeeId; }
}
