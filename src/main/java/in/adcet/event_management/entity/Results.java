package in.adcet.event_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class Results {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private int Id;
	
	
	@ManyToOne
	@JoinColumn(name = "winner")
	private User winnerUser;
	
	@ManyToOne
	@JoinColumn(name = "runnerUp")
	private User runnerUpUser;
	
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Events events;
	
	
}
