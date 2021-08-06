package fu.rms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "import")						// => import
@Getter
@Setter
public class Import extends Auditable {			//Import
	
	@Id
	@Column(name="import_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long importId;					// import_id importId
	
	@Column(name="import_code")				// import_code
	private String importCode;				// => importCode
	
	@Column(name="total_amount")		
	private Double totalAmount;			
	
	@Column(name="comment")
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supplier_id")
	private Supplier supplier;				
	
	@OneToMany(mappedBy = "imports",cascade = {CascadeType.PERSIST})
	private List<ImportMaterial> importMaterials;	
	
}
