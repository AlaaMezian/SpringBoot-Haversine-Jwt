package com.example.entity.base;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.example.constant.Constants;

@MappedSuperclass
public abstract class BaseEntity {

	@NotNull
	private Date createdAt;
	@NotNull
	private boolean active;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	public BaseEntity() {
		setStatus(Constants.ENTITY_STATUS_ACTIVE);
		createdAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStatus(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

}
