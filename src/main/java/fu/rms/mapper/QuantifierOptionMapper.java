package fu.rms.mapper;

import org.springframework.stereotype.Component;

import fu.rms.dto.MaterialQuantifierDto;
import fu.rms.dto.QuantifierOptionDto;
import fu.rms.entity.QuantifierOption;

@Component
public class QuantifierOptionMapper {
	
	public QuantifierOptionDto entityToDto(QuantifierOption quantifierOption) {
		QuantifierOptionDto quantifierOptionDto=new QuantifierOptionDto();
		quantifierOptionDto.setQuantifierOptionId(quantifierOption.getQuantifierOptionId());
		quantifierOptionDto.setQuantity(quantifierOption.getQuantity());
		quantifierOptionDto.setCost(quantifierOption.getCost());
		quantifierOptionDto.setDescription(quantifierOption.getDescription());
		if(quantifierOption.getMaterial()!=null) {
			MaterialQuantifierDto materialQuantifierDto=new MaterialQuantifierDto();
			materialQuantifierDto.setMaterialId(quantifierOption.getMaterial().getMaterialId());
			materialQuantifierDto.setMaterialName(quantifierOption.getMaterial().getMaterialName());	
			materialQuantifierDto.setUnit(quantifierOption.getMaterial().getUnit());	
			materialQuantifierDto.setUnitPrice(quantifierOption.getMaterial().getUnitPrice());	
			quantifierOptionDto.setMaterial(materialQuantifierDto);
		}
		return quantifierOptionDto;
	}
}
