package fu.rms.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory_material")						
@Getter
@Setter
public class InventoryMaterial extends Auditable{

	@Id
	@Column(name="inventory_material_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long inventoryMaterialId;		
	
	@Column(name="inventory_code")				// mã kiểm kê
	private String inventoryCode;
	
	@Column(name="inventory_date")
	private LocalDateTime inventoryDate;
	
	@Column(name="material_id")
	private Long materialId;
	
	@Column(name = "material_name")
	private String materialName;
	
	@Column(name = "material_unit")				// đơn vị
	private String materialUnit;
	
	@Column(name = "remain_system")				// remain trong material
	private Double remainSystem;
	
	@Column(name = "remain_fact")		
	private Double remainFact;					// remain thực tế của kiểm kho
	
	@Column(name = "quantity_different")		
	private Double quantityDifferent;			//lượng chênh lệch
	
	@Column(name = "reason")					// nguyên nhân
	private String reason;
	
	@Column(name = "process")	
	private Integer process;					// xử lý: import hay export, hay chưa xử lý
	
}
