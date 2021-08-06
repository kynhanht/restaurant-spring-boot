package fu.rms.dto;

public interface GetQuantifierMaterialDto {
	
	Long getDishId();

	Long getMaterialId();
	
	Double getQuantifier();
	
	Double getUnitPrice();
}
