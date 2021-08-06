package fu.rms.entity;

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
@Table(name = "export_material")		//import_material
@Getter
@Setter
public class ExportMaterial {
	@Id
	@Column(name="export_material_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long exportMaterialId;			// => importMaterialId
	
	@Column(name="quantity_export")			// số lượng  xuất)
	private Double quantityExport;			
	
	@Column(name="unit_price")					// giá giá xuất theo 1 đơn vị
	private Double unitPrice;					// => unitPrice, unit_price
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="export_id")
	private Export export;					// Import
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="material_id")
	private Material material;
	
}
