package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.MaterialQuantifierDto;
import fu.rms.dto.QuantifierDto;
import fu.rms.entity.Quantifier;

@Component
public class QuantifierMapper {
	

	public QuantifierDto entityToDto(Quantifier quantifier) {
		QuantifierDto quantifierDto=new QuantifierDto();
		quantifierDto.setQuantifierId(quantifier.getQuantifierId());
		quantifierDto.setQuantity(quantifier.getQuantity());
		quantifierDto.setCost(quantifier.getCost());
		quantifierDto.setDescription(quantifier.getDescription());
		if(quantifier.getMaterial()!=null) {
			MaterialQuantifierDto materialQuantifierDto=new MaterialQuantifierDto();
			materialQuantifierDto.setMaterialId(quantifier.getMaterial().getMaterialId());
			materialQuantifierDto.setMaterialName(quantifier.getMaterial().getMaterialName());	
			materialQuantifierDto.setUnit(quantifier.getMaterial().getUnit());	
			materialQuantifierDto.setUnitPrice(quantifier.getMaterial().getUnitPrice());	
			quantifierDto.setMaterial(materialQuantifierDto);
		}
		return quantifierDto;
	}
	
}
