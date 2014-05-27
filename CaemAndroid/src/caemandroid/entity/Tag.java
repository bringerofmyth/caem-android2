package caemandroid.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Tag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794606974735780763L;
	private Integer id;
	private String title;
	private Set<Event> events = new HashSet<Event>(0);
	private Set<Place> places = new HashSet<Place>(0);


	public Integer getId() {
		return this.id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		Tag other = (Tag) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Set<Event> getEvents() {
		return this.events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}


	public Set<Place> getPlaces() {
		return this.places;
	}

	public void setPlaces(Set<Place> places) {
		this.places = places;
	}


}
