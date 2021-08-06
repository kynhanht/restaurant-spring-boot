package fu.rms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fu.rms.dto.LocationTableDto;
import fu.rms.service.ILocationTableService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class LocationTableController {

	@Autowired
	private ILocationTableService locationTableService;
	
	@GetMapping("/location-table/all")
	public List<LocationTableDto> getList(){
		
		return locationTableService.findAll();
	}
	@GetMapping("/location-table/{id}")
	public LocationTableDto getLocationTable(@PathVariable(name = "id") Long locationId){
		
		return locationTableService.findByLocationId(locationId);
	}

}
