package caemandroid.entity;

import java.io.Serializable;
import java.util.Date;




public class Registration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5108109609317586171L;
	private Integer id;
	private Event event;
	private User user;
	private Date timeOfRegistration;
	private String status;


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}


	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public Date getTimeOfRegistration() {
		return this.timeOfRegistration;
	}

	public void setTimeOfRegistration(Date timeOfRegistration) {
		this.timeOfRegistration = timeOfRegistration;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		Registration other = (Registration) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}




}
