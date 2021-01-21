package beans.value_objects;

import java.time.LocalDateTime;

public class DateRange {
	private LocalDateTime start;
	private LocalDateTime end;
	
	public DateRange(LocalDateTime start, LocalDateTime end) throws Exception {
		validate(start, end);
		this.start = start;
		this.end = end;
	}
	
	public void validate(LocalDateTime start, LocalDateTime end) throws Exception {
		if(end.isBefore(start)) 
			throw new Exception();
	}
	
	public boolean inRange(LocalDateTime date) {
		return (date.isBefore(end) && date.isAfter(start)); 
	}
}
