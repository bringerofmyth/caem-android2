package caemandroid.entity;

import java.io.Serializable;

public class EventsListObject implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1644360484299411501L;
	private String id, name, category;
	private int imgId;
	private  boolean isSelected;

	public EventsListObject(String id, String name, String category) {
		this.id = id;
		this.name = name;
		this.setCategory(category);
	}


	public EventsListObject(String id, String name, String category,int imgIds) {
		this.id = id;
		this.name = name;
		this.setCategory(category);
		this.imgId = imgIds;
	}
	public EventsListObject(String id, String name,int imgIds) {
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


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public boolean isSelected() {
		return isSelected;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
