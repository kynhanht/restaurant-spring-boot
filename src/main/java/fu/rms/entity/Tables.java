package fu.rms.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tables")
@Getter
@Setter
public class Tables {

	@Id
	@Column(name="table_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long tableId;
	
	@Column(name="table_code")
	private String tableCode;
	
	@Column(name="table_name")
	private String tableName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="location_id")
	private LocationTable locationTable;
	
	@Column(name="min_capacity")
	private Integer minCapacity;
	
	@Column(name="max_capacity")
	private Integer maxCapacity;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;

}
