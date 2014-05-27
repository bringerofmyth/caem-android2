package caemandroid.entity;
;
public class NamingValues {

	public enum Role{
		Admin,
		EventOwner,
		Attender;

		@Override
		public String toString() {
			switch(this) {
			case Admin: return "Admin";
			case EventOwner: return "Event Owner";
			case Attender: return "Attender";
			default: throw new IllegalArgumentException();
			}

		}

	}

	public enum EventType {
		EventType1, EventType2, EventType3;
		@Override
		public String toString() {
			switch (this) {
			case EventType1:
				return "EventType1";
			case EventType2:
				return "EventType2";
			case EventType3:
				return "EventType3";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}