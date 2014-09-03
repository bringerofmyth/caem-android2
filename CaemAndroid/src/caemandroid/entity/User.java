package caemandroid.entity;

import java.io.Serializable;



import caemandroid.entity.NamingValues.Role;


public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6515616387216885728L;
	private Integer id;
	private String name;
	private String surname;
	private String userName;
	private String password;
	private String email;
	private String phone;
	private String weatherProfile;
	private String timeProfile;
	private String locationProfile;
	private String location;
	
	
	private Role role;



	public String getWeatherProfile() {
		return weatherProfile;
	}
	public void setWeatherProfile(String weatherProfile) {
		this.weatherProfile = weatherProfile;
	}
	public String getTimeProfile() {
		return timeProfile;
	}
	public void setTimeProfile(String timeProfile) {
		this.timeProfile = timeProfile;
	}
	public String getLocationProfile() {
		return locationProfile;
	}
	public void setLocationProfile(String locationProfile) {
		this.locationProfile = locationProfile;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer iD) {
		this.id = iD;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}





}
