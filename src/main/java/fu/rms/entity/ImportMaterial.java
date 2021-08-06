package fu.rms.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "import_material")		//import_material
@Getter
@Setter
public class ImportMaterial {			//ImportMaterial

	@Id
	@Column(name="import_material_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long importMaterialId;			// => importMaterialId
	
	@Column(name="quantity_import")			 
	private Double quantityImport;			
	
	@Column(name="unit_price")					// giá giá nhập theo 1 đơn vị
	private Double unitPrice;					// => unitPrice, unit_price
	
	@Column(name="sum_price")				// tổng giá nhập/xuất cho 1 nvl
	private Double sumPrice;
	
	@Column(name="expire_date")
	private LocalDateTime expireDate;			
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="import_id")
	private Import imports;					// Import
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="material_id")
	private Material material;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="warehouse_id")
	private Warehouse warehouse;			// chỉ import mới có
}
