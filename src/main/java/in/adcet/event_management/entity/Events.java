package in.adcet.event_management.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Events {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private int ID;
	
	@NonNull
	@Column(nullable = false)
	private String name;

	@NonNull
	private String code;
	
	@NonNull
	@Column(nullable = false)
	private LocalDate eventDate;

	private LocalDate endDate=eventDate;

	@NonNull
	private String venue;

	private LocalTime time;
	
	@NonNull
	private String description;
	
	@NonNull
	private String category;

	private String status="ONGOING";

	private LocalDate registrationDeadline;
	
	private int maxParticipant;

	private int registrationCount;

	private String organizer="ADCET";

	private String bannerPath;

	@OneToMany(mappedBy = "events", cascade = CascadeType.ALL)
	private List<Register> registers;
	
	@OneToMany(mappedBy = "events", cascade = CascadeType.ALL)
	private List<Results> results;

}
