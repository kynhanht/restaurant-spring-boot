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
@Table(name = "warehouses")
@Getter
@Setter
public class Warehouse {
	
	@Id
	@Column(name="warehouse_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long warehouseId;
	
	@Column(name="name")
	private String name;
}
