package in.adcet.event_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ResultDTO {
	private String username;
	private String name;
	private String eventName;
	private String winner;
	private String runnerup;
}
