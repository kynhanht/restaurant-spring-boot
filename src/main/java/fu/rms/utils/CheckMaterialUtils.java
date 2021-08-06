package fu.rms.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fu.rms.dto.DishInOrderDishDto;
import fu.rms.dto.GetQuantifierMaterialDto;

public class CheckMaterialUtils {
	
	
	public static Map<Long, Double> testKho(Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> map) {
		
		Map<Long, Double> sumMaterial = null;
		try {
			if(map != null) {

				sumMaterial = new HashMap<Long, Double>();
				Double sumQuantifierByMaterial = (double) 0;											
				Double sum = 0d;
				for (DishInOrderDishDto dish : map.keySet()) {
					List<GetQuantifierMaterialDto> listQuantifier = map.get(dish);
					for (int i = 0; i < listQuantifier.size(); i++) {
						
						if(sumMaterial.get(listQuantifier.get(i).getMaterialId()) != null) {		// neu co key roi thi add them vao value cu
							sumQuantifierByMaterial = sumMaterial.get(listQuantifier.get(i).getMaterialId()); // lấy giá trị hiện tại
							sum = Utils.sumBigDecimalToDouble(sumQuantifierByMaterial, listQuantifier.get(i).getQuantifier()*dish.getQuantity());		// cộng thêm thằng mới
							sumMaterial.replace(listQuantifier.get(i).getMaterialId(), sum);	// put lại vào
							
						}else {															// nếu chưa có thì put mới
							sum = Utils.bigDecimalToDouble(listQuantifier.get(i).getQuantifier()*dish.getQuantity());
							sumMaterial.put(listQuantifier.get(i).getMaterialId(), sum);
						}
					}								
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sumMaterial;
	}
	
	public static Map<Long, Double> checkKho(Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> map) {
		
		Map<Long, Double> sumMaterial = null;
		try {
			if(map != null) {
				sumMaterial = new HashMap<Long, Double>();
				Double sumQuantifierByMaterial = (double) 0;											
				Double sum = 0d;
				for (DishInOrderDishDto dish : map.keySet()) {
					List<GetQuantifierMaterialDto> listQuantifier = map.get(dish);
					for (int i = 0; i < listQuantifier.size(); i++) {
						if(sumMaterial.get(listQuantifier.get(i).getMaterialId()) != null) {		// neu co key roi thi add them vao value cu
							sumQuantifierByMaterial = sumMaterial.get(listQuantifier.get(i).getMaterialId()); // lấy giá trị hiện tại
							sum = Utils.sumBigDecimalToDouble(sumQuantifierByMaterial, listQuantifier.get(i).getQuantifier()*dish.getQuantity());		// cộng thêm thằng mới
							sumMaterial.replace(listQuantifier.get(i).getMaterialId(), sum);	// put lại vào
							
						}else {															// nếu chưa có thì put mới
							sum = Utils.bigDecimalToDouble(listQuantifier.get(i).getQuantifier()*dish.getQuantity());
							sumMaterial.put(listQuantifier.get(i).getMaterialId(), sum);
						}
					}
				}								
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sumMaterial;
	}
	
	public static Map<Long, Double> calculateMaterial(Map<Long, List<GetQuantifierMaterialDto>> map) {
		
		Map<Long, Double> material = null;
		try {
			if(map != null) {
				material = new HashMap<Long, Double>();
				Double sumQuantifierByMaterial = (double) 0;											
				Double sum = 0d;
				for (Long dish : map.keySet()) {
					List<GetQuantifierMaterialDto> listQuantifier = map.get(dish);
					for (int i = 0; i < listQuantifier.size(); i++) {
						if(material.get(listQuantifier.get(i).getMaterialId()) != null) {		// neu co key roi thi add them vao value cu
							sumQuantifierByMaterial = material.get(listQuantifier.get(i).getMaterialId()); // lấy giá trị hiện tại
							sum = Utils.sumBigDecimalToDouble(sumQuantifierByMaterial, listQuantifier.get(i).getQuantifier());		// cộng thêm thằng mới
							material.replace(listQuantifier.get(i).getMaterialId(), sum);	// put lại vào
							
						}else {															// nếu chưa có thì put mới
							sum = Utils.bigDecimalToDouble(listQuantifier.get(i).getQuantifier());
							material.put(listQuantifier.get(i).getMaterialId(), sum);
						}
					}
				}								
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return material;
	}

}
