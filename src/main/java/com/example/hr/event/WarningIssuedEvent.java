package com.example.hr.event;

import com.example.hr.enums.WarningLevel;

public class WarningIssuedEvent extends HrEvent {
    private final Integer warningId;
    private final Integer employeeId;
    private final WarningLevel level;

    public WarningIssuedEvent(Object source, String actor, Integer warningId,
                                Integer employeeId, WarningLevel level) {
        super(source, actor, "WARNING_ISSUED");
        this.warningId = warningId;
        this.employeeId = employeeId;
        this.level = level;
    }

    public Integer getWarningId() { return warningId; }
    public Integer getEmployeeId() { return employeeId; }
    public WarningLevel getLevel() { return level; }
}
