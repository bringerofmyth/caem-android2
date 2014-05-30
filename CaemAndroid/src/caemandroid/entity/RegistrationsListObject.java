package caemandroid.entity;

import java.io.Serializable;

public class RegistrationsListObject implements Serializable {

	private String id, name, date;
	private int imgId;
	private  boolean isSelected;

	public RegistrationsListObject(String id, String name, String date) {
		this.id = id;
		this.name = name;
		this.setDate(date);
	}


	public RegistrationsListObject(String id, String name, String date,int imgIds) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.imgId = imgIds;
	}
	public RegistrationsListObject(String id, String name,int imgIds) {
		this.id = id;
		this.name = name;
		this.imgId = imgIds;
	}
	public String getId() {
		return id;
	}

	public int getImgId() {
		return imgId;
	}


	public void setImgId(int imgId) {
		this.imgId = imgId;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getListString(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return this.name;
		
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public boolean isSelected() {
		return isSelected;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}


