package com.example.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="images")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "fc_status")
	private String formClassifierStatus;
	
	@Column(name = "fc_json")
	private String formClassifierJson;

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFormClassifierStatus() {
		return formClassifierStatus;
	}

	public void setFormClassifierStatus(String formClassifierStatus) {
		this.formClassifierStatus = formClassifierStatus;
	}

	public String getFormClassifierJson() {
		return formClassifierJson;
	}

	public void setFormClassifierJson(String formClassifierJson) {
		this.formClassifierJson = formClassifierJson;
	}
	
	
}
