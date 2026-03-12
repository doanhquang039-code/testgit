package com.example.hr.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "contract_type", length = 50)
    private String contractType;

    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "base_salary_on_contract", precision = 15, scale = 2)
    private BigDecimal baseSalaryOnContract;

    @Column(name = "contract_image")
    private String contractImage;

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

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public LocalDate getSignDate() {
		return signDate;
	}

	public void setSignDate(LocalDate signDate) {
		this.signDate = signDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigDecimal getBaseSalaryOnContract() {
		return baseSalaryOnContract;
	}

	public void setBaseSalaryOnContract(BigDecimal baseSalaryOnContract) {
		this.baseSalaryOnContract = baseSalaryOnContract;
	}

	public String getContractImage() {
		return contractImage;
	}

	public void setContractImage(String contractImage) {
		this.contractImage = contractImage;
	}
}
