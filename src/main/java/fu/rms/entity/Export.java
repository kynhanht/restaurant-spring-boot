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
@Table(name = "export")
@Setter
@Getter
public class Export extends Auditable{

	@Id
	@Column(name="export_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long exportId;					// import_id importId
	
	@Column(name="export_code")				// import_code
	private String exportCode;				// => importCode
	
	@Column(name="comment")
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;			// chỉ import mới có
	
	@OneToMany(mappedBy = "export", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	private List<ExportMaterial> exportMaterials;	
}
