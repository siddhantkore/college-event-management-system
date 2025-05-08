package in.adcet.event_management.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private int Id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	private String role="student";
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Register> registers;
	
	@OneToMany(mappedBy = "winnerUser", cascade = CascadeType.ALL)
	private List<Results> winnerResults;
	
	@OneToMany(mappedBy = "runnerUpUser", cascade = CascadeType.ALL)
	private List<Results> runnerUpResults;
	
}
