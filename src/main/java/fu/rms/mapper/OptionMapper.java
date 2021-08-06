package fu.rms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fu.rms.dto.OptionDto;
import fu.rms.dto.QuantifierOptionDto;
import fu.rms.entity.Option;
@Component
public class OptionMapper {

	@Autowired
	private QuantifierOptionMapper quantifierOptionMapper;
	
	public OptionDto entityToDto(Option option) {
		
		OptionDto optionDto=new OptionDto();
		optionDto.setOptionId(option.getOptionId());
		optionDto.setOptionName(option.getOptionName());
		optionDto.setOptionType(option.getOptionType());
		optionDto.setUnit(option.getUnit());
		optionDto.setPrice(option.getPrice());
		optionDto.setCost(option.getCost());
		optionDto.setOptionCost(option.getOptionCost());
		if(option.getQuantifierOptions()!=null) {
			List<QuantifierOptionDto> quantifierOptionDtos=option.getQuantifierOptions()
					.stream().map(quantifierOptionMapper::entityToDto).collect(Collectors.toList());
			optionDto.setQuantifierOptions(quantifierOptionDtos);
		}
		return optionDto;
	}
}
