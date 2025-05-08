package in.adcet.event_management.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class RegistrationDTO {
	private String username;
	private String email;
	private String code;
	private String name;
	private LocalDate eventDate;
	private String status;
	
	public RegistrationDTO(String username, String email, String eventName) {
		this.username = username;
		this.email = email;
		this.name = eventName;
	}
	
	public RegistrationDTO(String eventName, LocalDate evenDate) {
		this.name = eventName;
		this.eventDate = evenDate;
	}
}
