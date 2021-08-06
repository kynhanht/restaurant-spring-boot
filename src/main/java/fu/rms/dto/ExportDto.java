package fu.rms.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportDto {

	private Long exportId;
	
	private String exportCode;

	private String comment;
	
	private List<ExportMaterialDto> exportMaterials;
}
