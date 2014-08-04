package caemandroid.entity;

import java.io.Serializable;

public class EventDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6814547300362407502L;
	private Integer id;
	private String title;
	private String weatherStatus;
	private String eventType;
	private String startTime;
	private String finishTime;
	private String ruleFireStatus;
	private String 	Message;
	private String Approval;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getRuleFireStatus() {
		return ruleFireStatus;
	}
	public void setRuleFireStatus(String ruleFireStatus) {
		this.ruleFireStatus = ruleFireStatus;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getApproval() {
		return Approval;
	}
	public void setApproval(String approval) {
		Approval = approval;
	}
	public String getWeatherStatus() {
		return weatherStatus;
	}
	public void setWeatherStatus(String weatherStatus) {
		this.weatherStatus = weatherStatus;
	}
	

	
	
}
