package caemandroid.entity;

import java.io.Serializable;

public class InterestsListObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7479772793471146378L;
	/**
	 * 
	 */

	private String id, name;
	private  boolean isSelected;

	public InterestsListObject(String id, String name) {
		this.id = id;
		this.name = name;
	}


	public InterestsListObject() {

	}
	public String getId() {
		return id;
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




	public boolean isSelected() {
		return isSelected;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
