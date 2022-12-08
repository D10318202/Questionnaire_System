package com.QuesSystem.ques.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "oftenuse")
@XmlRootElement
@NamedQuery(name = "oftenuse.findAll", query = "SELECT often FROM OftenUseQuestion often")
public class OftenUseQuestion {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "oftenuse_id", unique = true)
	private String Id;
	
	@Column(name = "oftenuse_title", length=15)
	private String Title;

	@Column(name = "oftenuse_choices")
	private String Choices;
	
	@Column(name = "oftenuse_type")
	private int Type;

	@Column(name = "must_keyin")
	private boolean mustKeyin;
		
	@Column(name="create_date")
	private Date createDate = new Date();

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getChoices() {
		return Choices;
	}

	public void setChoices(String choices) {
		Choices = choices;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public boolean isMustKeyin() {
		return mustKeyin;
	}

	public void setMustKeyin(boolean mustKeyin) {
		this.mustKeyin = mustKeyin;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}