package fu.rms.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "statuses")
@Getter
@Setter
public class Status {
	@Id
	@Column(name = "status_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long statusId;
	
	@Column(name = "status_name")
	private String statusName;
	
	@Column(name="status_value")
	private String statusValue;
	
	@Column(name="status_description")
	private String statusDescription;

}
