package com.example.hr.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.hr.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "taskassignment")
@Data
public class TaskAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id")
	private Task task;

	@Column(name = "assigned_date", nullable = false)
	private LocalDate assignedDate;

	@Enumerated(EnumType.STRING)
	private TaskStatus status = TaskStatus.PENDING;

	@Column(name = "actual_hours", precision = 5, scale = 2)
	private BigDecimal actualHours;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public LocalDate getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDate assignedDate) {
		this.assignedDate = assignedDate;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public BigDecimal getActualHours() {
		return actualHours;
	}

	public void setActualHours(BigDecimal actualHours) {
		this.actualHours = actualHours;
	}
}