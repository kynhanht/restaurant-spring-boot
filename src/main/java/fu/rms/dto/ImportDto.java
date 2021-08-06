package fu.rms.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportDto {

	private Long importId;
	
	private String importCode;
	
	private Double totalAmount;
	
	private String comment;
	
    private String createdBy;

    private String createdDate;
    
    private String lastModifiedBy;
  
    private String lastModifiedDate;
	
	private SupplierDto supplier;
	
	private List<ImportMaterialDto> importMaterials;
	
}
