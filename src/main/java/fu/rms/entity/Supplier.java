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
@Table(name = "suppliers")
@Getter
@Setter
public class Supplier {
	
	@Id
	@Column(name="supplier_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long supplierId;
	
	@Column(name="name")
	private String supplierName;
	
	@Column(name="phone")
	private String phone;
}
