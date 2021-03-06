package fu.rms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.constant.AppMessageErrorConstant;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.DishInOrderDishDto;
import fu.rms.dto.GetQuantifierMaterialDto;
import fu.rms.dto.OrderDishCancelDto;
import fu.rms.dto.OrderDishChefDto;
import fu.rms.dto.OrderDishDto;
import fu.rms.dto.OrderDishOptionDto;
import fu.rms.dto.RemainDto;
import fu.rms.dto.SumQuantityAndPriceDto;
import fu.rms.entity.Export;
import fu.rms.entity.ExportMaterial;
import fu.rms.entity.Material;
import fu.rms.entity.Option;
import fu.rms.entity.OrderDish;
import fu.rms.entity.OrderDishOption;
import fu.rms.entity.Status;
import fu.rms.exception.NotFoundException;
import fu.rms.exception.NullPointerException;
import fu.rms.mapper.OrderDishMapper;
import fu.rms.repository.ExportRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.repository.OptionRepository;
import fu.rms.repository.OrderDishOptionRepository;
import fu.rms.repository.OrderDishRepository;
import fu.rms.repository.OrderRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.request.OrderDishChefRequest;
import fu.rms.request.OrderDishRequest;
import fu.rms.service.IOrderDishService;
import fu.rms.utils.CheckMaterialUtils;
import fu.rms.utils.Utils;

@Service
public class OrderDishService implements IOrderDishService {
	
	@Autowired
	private OrderDishMapper orderDishMapper;
	
	@Autowired
	private OrderDishRepository orderDishRepo;
	
	@Autowired
	private OrderDishOptionService orderDishOptionService;
	
	@Autowired
	private OrderDishOptionRepository orderDishOptionRepo;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private TableService tableService;
	
	@Autowired
	private OrderDishCancelService orderDishCancelService;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private StatusRepository statusRepo;

	@Autowired
	private OptionRepository optionRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private MaterialRepository materialRepo;
	
	@Autowired
	private ExportRepository exportRepo;


//	/**
//	 * danh s??ch m??n ??n trong order
//	 */
//	@Override
//	public List<OrderDishDto> getListOrderDishByOrder(Long orderId) {
//		List<OrderDishDto> listDto = null;
//		try {
//			if(orderId != null) {
//				List<OrderDish> listOrderDish = orderDishRepo.findByOrder(orderId);
//				listDto = listOrderDish.stream().map(orderDishMapper::entityToDto)
//						.collect(Collectors.toList());	
//				
//				for (int i = 0; i < listOrderDish.size(); i++) {
//					List<OrderDishOptionDto> listOrderDishOption = new ArrayList<OrderDishOptionDto>();
//					if(listDto.get(i).getOrderDishOptions() != null && listDto.get(i).getOrderDishOptions().size() != 0) {
//						
//						listOrderDishOption = listOrderDish.get(i).getOrderDishOptions()
//						.stream().map(orderDishOptionMapper::entityToDto).collect(Collectors.toList());	;
//					}
//					listDto.get(i).setOrderDishOptions(listOrderDishOption);
//				}
//			}
//		} catch (Exception e) {
//			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
//		}
//		
//		return listDto;
//	}

	/**
	 * th??m m??n khi order
	 */
	@Override
	public Long insertOrderDish(OrderDishDto dto, Long orderId) {

		int result =  0;
		Long orderDishId = (long) 0;
		try {
			if(dto != null && orderId != null) {
				result = orderDishRepo.insertOrderDish(orderId, dto.getDish().getDishId(), dto.getComment(),
						dto.getQuantity(), 0, dto.getQuantity(), dto.getSellPrice(), dto.getSumPrice(), dto.getCreateBy(), Utils.getCurrentTime(),
						StatusConstant.STATUS_ORDER_DISH_ORDERED);
			}
			if(result != 0) {
				orderDishId = orderDishRepo.findLastestOrderDishId(orderId);
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return orderDishId;
	}

	/**
	 * c???p nh???t order: order dish: gi??, s??? l?????ng
	 */
	@Override
	@Transactional
	public String updateQuantityOrderDish(OrderDishDto dto) {
		if(dto!= null && dto.getOrderOrderId() != null) {
			try {
				int addQuantity = 0;
				OrderDish orderDish = orderDishRepo.findById(dto.getOrderDishId())
						.orElseThrow(()-> new NotFoundException("Not found Dish: "+dto.getDish().getDishName()));
																							// tang so luong thi moi xet nvl
				boolean check = true;
				boolean checkIncrease = false;
				boolean checkDecrease = false;
				Map<Long, Double> map = null;
				// t??nh nguy??n v???t li???u
				DishInOrderDishDto dish = new DishInOrderDishDto();
				dish.setDishId(orderDish.getDish().getDishId());
				dish.setOrderDishId(orderDish.getOrderDishId());
				if(orderDish.getQuantityOk() < dto.getQuantityOk()) {											// tang so luong
					dish.setQuantity(dto.getQuantityOk() - orderDish.getQuantityOk());
					checkIncrease = true;
				}else if(orderDish.getQuantityOk() > dto.getQuantityOk()){
					dish.setQuantity(orderDish.getQuantityOk() - dto.getQuantityOk());							// giam so luong
					checkDecrease = true;
				}else {
					dish.setQuantity(0);
					if(dto.getSellPrice().equals(orderDish.getSellPrice())) {										
						return AppMessageErrorConstant.NO_CHANGE_DATA;
					}else {																						// ch??? thay ?????i gi?? ti???n
						orderDish.setSellPrice(dto.getSellPrice());														
						orderDish.setSumPrice(dto.getSumPrice());
						orderDish.setModifiedBy(dto.getModifiedBy());
						orderDish.setModifiedDate(Utils.getCurrentTime());
						orderDishRepo.save(orderDish);
						SumQuantityAndPriceDto sum = getSumQtyAndPriceByOrder(dto.getOrderOrderId());				// c???p nh???t l???i order
						orderService.updateOrderQuantity(sum.getSumQuantity(), sum.getSumPrice(), dto.getOrderOrderId());
						simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderOrderId(), orderService.getOrderDetailById(dto.getOrderOrderId()));
						return "";
					}
				}
				if(checkDecrease || checkIncrease) {															// ho???c t??ng ho???c gi???m
					Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> mapDish = new HashMap<DishInOrderDishDto, List<GetQuantifierMaterialDto>>();
					List<GetQuantifierMaterialDto> listQuantifier = new ArrayList<GetQuantifierMaterialDto>();
					listQuantifier = orderRepo.findListQuantifierMaterialByDish(dish.getDishId());
					if(listQuantifier.size() != 0) {
						mapDish.put(dish, listQuantifier);
						map = CheckMaterialUtils.checkKho(mapDish);													// t??m ra l?????ng nvl khi t??ng gi???m s??? l?????ng m??n ??n
						check=false;		
					}
					// t??nh ra ???????c s??? l?????ng nvl
					
					if(checkIncrease&&!check) {																	// n???u c?? nvl v?? t??ng th?? m???i check xem trong kho c??n ko
						int max = 0;
						String messageMaterial = "";
						boolean checkMax = false;
						Map<Long, List<GetQuantifierMaterialDto>> mapDish2 = new HashMap<Long, List<GetQuantifierMaterialDto>>();
						mapDish2.put(orderDish.getOrderDishId(), listQuantifier);
						Map<Long, Double> map2 = CheckMaterialUtils.calculateMaterial(mapDish2);
						for (Long materialId : map.keySet()) {
							RemainDto remain = materialRepo.findRemainById(materialId);
							Double remainMaterial = remain.getRemain();
							if(map.get(materialId) > remainMaterial) {												// neu nvl can > nvl con lai
								check = true;
								messageMaterial += remain.getMaterialName() + ", ";
								for (Long materialIdDish : map2.keySet()) {									// map ch???a dish c???a 1 th???ng
									if(materialIdDish.equals(materialId)) {
										if(max == 0 && !checkMax) {																	// l???n ?????u t??m ??c nvl
											double quantity = remainMaterial/map2.get(materialIdDish);
											max = (int) quantity;
										}else {
											double quantity = remainMaterial/map2.get(materialIdDish);
											if((int) quantity < max) {													// t??m dc th???ng kh??c nh??? h??n
												max = (int) quantity;
											}
										}
										checkMax = true;
										break;
									}
								}																									
							}
						}
						if(check) {																					//c?? nvl trong m??n ???? ko ????? ????? th???c hi???n	
							messageMaterial = messageMaterial.substring(0, messageMaterial.length()-2);
							String message="Kh??ng ????? nguy??n li???u " + messageMaterial + ", ";																		// m??n k ????? nvl
							max += orderDish.getQuantityOk();														// t???i ??a c?? th??? th???c hi???n ???????c
							message += orderDish.getDish().getDishName() + " ch??? th???c hi???n ???????c t???i ??a " + max + " " + orderDish.getDish().getDishUnit();
							return message;																			//s??? l?????ng c?? th??? ?????
						}
					}
				}
				boolean checkChange=false;
				//neu ko thieu nvl thi tang giam thoai mai
				if(orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_ORDERED) {				// n???u l?? ordered t??ng gi???m ?????u ???????c
					orderDish.setQuantity(dto.getQuantityOk());														// n???u l?? ordered th?? ko t??ng gi???m ???????c, ko c???n check 
					orderDish.setQuantityOk(dto.getQuantityOk());
					orderDish.setSellPrice(dto.getSellPrice());
					orderDish.setSumPrice(dto.getSumPrice());
					orderDish.setModifiedBy(dto.getModifiedBy());
					orderDish.setModifiedDate(Utils.getCurrentTime());
					orderDishRepo.save(orderDish);
					
				} else if (orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_COMPLETED 
						|| orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_PREPARATION) {	// n???u l?? completed ho???c prepare th?? ch??? t??ng s??? l?????ng (insert th??m l?????ng ch??nh)

					if(orderDish.getQuantityOk() >= dto.getQuantityOk()) {											// ko ??c gi???m
						return AppMessageErrorConstant.INPUT_WRONG;
					}else {
						if(dto.getSellPrice().equals(orderDish.getSellPrice())) {										
							// ko s???a th???ng hi???n t???i, ch??? th??m th???ng m???i
						}else {																						// ch??? thay ?????i gi?? ti???n
							checkChange=true;
						}
						OrderDishDto orderDishDto = getOrderDishById(dto.getOrderDishId());
						addQuantity = dto.getQuantityOk() - orderDishDto.getQuantityOk();							// t??ng ch??? c???ng th??m
						
						OrderDish orderDishNewDish = orderDishMapper.dtoToEntity(orderDishDto);
						orderDishNewDish.setQuantity(addQuantity);
						orderDishNewDish.setQuantityOk(addQuantity);
						orderDishNewDish.setQuantityCancel(0);
						orderDishNewDish.setSellPrice(dto.getSellPrice());
						orderDishNewDish.setSumPrice(dto.getSellPrice()*addQuantity);
						orderDishNewDish.setCreateBy(dto.getModifiedBy());										// th???ng thay ?????i l?? th???ng t???o
						orderDishNewDish.setCreateDate(Utils.getCurrentTime());
						orderDishNewDish.setOrderDishId(null);
						orderDishRepo.save(orderDishNewDish);													// t???o m???i ra th???ng kh??c
						Long orderDishId = orderDishRepo.findLastestOrderDishId(orderDishDto.getOrderOrderId());
						orderDishNewDish.setOrderDishId(orderDishId);
						List<OrderDishOption> listOdo = null;
						OrderDishOption odo = null;
						Long orderDishOptionId = null;
						if(orderDishDto.getOrderDishOptions().size() != 0) {									// t???o m???i c??c th???ng topping
							listOdo = new ArrayList<OrderDishOption>();
							for (OrderDishOptionDto odoNew : orderDishDto.getOrderDishOptions()) {
								odo = new OrderDishOption();
								orderDishOptionRepo.insert(orderDishId, odoNew.getOptionId(), odoNew.getQuantity(), 
										odoNew.getSumPrice(), odoNew.getOptionPrice(), StatusConstant.STATUS_ORDER_DISH_OPTION_DONE);
								odo.setQuantity(odoNew.getQuantity());
								odo.setSumPrice(odoNew.getSumPrice());
								odo.setUnitPrice(odoNew.getOptionPrice());
								Status statusOdo = statusRepo.findById(StatusConstant.STATUS_ORDER_DISH_OPTION_DONE).get();
								Option option = optionRepo.findById(odoNew.getOptionId()).get();
								odo.setOption(option);
								odo.setStatus(statusOdo);
								odo.setOrderDish(orderDishNewDish);
								orderDishOptionId = orderDishOptionRepo.findLastestOrderDishOptionId(orderDishId);
								odo.setOrderDishOptionId(orderDishOptionId);
								listOdo.add(odo);
							}
						}
						orderDishNewDish.setOrderDishOptions(listOdo);
					}		
				}
				if(checkChange) {											//xu???ng ????y m???i ch???y socket
					orderDish.setSellPrice(dto.getSellPrice());														
					orderDish.setSumPrice(dto.getSellPrice()*orderDish.getQuantityOk());
					orderDish.setModifiedBy(dto.getModifiedBy());
					orderDish.setModifiedDate(Utils.getCurrentTime());
					orderDishRepo.save(orderDish);							// s???a l???i th???ng c??
				}
				// s???a l???i export tr?????c ????
				if(map != null) {																					// c?? nvl
					Export export = null;																			// t??ng s??? l?????ng
					Long exportId = exportRepo.findByOrderId(orderDish.getOrder().getOrderId());						// l???y ra export id theo order id
					if(exportId != null) {
						export = exportRepo.findById(exportId).orElseThrow(
								() -> new NotFoundException("Not found Export: " + exportId));
						
						List<ExportMaterial> listExportMaterial = new ArrayList<ExportMaterial>();		
						Material material = null;
						Double remainNew = 0d, totalExportNew = 0d, quantityExportNew = 0d;
						for (Long materialId : map.keySet()) {															// upadate l???i material, exportmaterial
							for (ExportMaterial exportMaterial : export.getExportMaterials()) {
								if(materialId == exportMaterial.getMaterial().getMaterialId()) {						// t??m material li??n quan ?????n m??n ??n ????
									material = exportMaterial.getMaterial();											// l???y ra material ????
									if(checkIncrease) {																	// t??ng s??? l?????ng
										
										remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));			// remain c??n l???i: tr??? ??i s??? l?????ng export
										totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));		// t??ng l??n s??? l?????ng export
										quantityExportNew = Utils.sumBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update l???i s??? l?????ng export
										
									}else {																				// gi???m ??? tr?????ng h???p ordered
										
										remainNew = Utils.sumBigDecimalToDouble(material.getRemain(), map.get(materialId));			// remain c??n l???i: tr??? ??i s??? l?????ng export
										totalExportNew = Utils.subtractBigDecimalToDouble(material.getTotalExport(), map.get(materialId));		// t??ng l??n s??? l?????ng export
										quantityExportNew = Utils.subtractBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update l???i s??? l?????ng export
									}
									material.setTotalExport(totalExportNew);
									material.setRemain(remainNew);
									exportMaterial.setMaterial(material);
									exportMaterial.setQuantityExport(quantityExportNew);
									listExportMaterial.add(exportMaterial);											// l??u l???i v??o list
									break;
								}
							}
						}
						export.setExportMaterials(listExportMaterial);												// l??u l???i v??o export
						exportRepo.save(export);																	// l??u v??o database
						// end s???a export
					}
					
				}
				
				orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_ORDERED, dto.getOrderOrderId());		// c???p nh???t l???i order sang tr???ng th??i ordered
				SumQuantityAndPriceDto sum = getSumQtyAndPriceByOrder(dto.getOrderOrderId());	
				orderService.updateOrderQuantity(sum.getSumQuantity(), sum.getSumPrice(), dto.getOrderOrderId());
				
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderOrderId(), orderService.getOrderDetailById(dto.getOrderOrderId()));
				simpMessagingTemplate.convertAndSend("/topic/chef", orderService.getListDisplayChefScreen());
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
			} catch (NullPointerException e) {
				throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
			}
		}
		return "";
	}

	/**
	 * select sum quantity and price by order
	 */
	@Override
	public SumQuantityAndPriceDto getSumQtyAndPriceByOrder(Long orderId) {
		SumQuantityAndPriceDto sum = null;
		if(orderId != null) {
			sum = orderDishRepo.findSumQtyAndPrice(orderId, StatusConstant.STATUS_ORDER_DISH_CANCELED);
		}
		return sum;
	}

	/**
	 * c???p nh???t l???i topping
	 */
	@Override
	@Transactional
	public int updateToppingComment(OrderDishDto dto) {
		int result = 0;
		try {
			if(dto!= null && dto.getOrderDishId() != null && dto.getOrderOrderId() != null && dto.getOrderDishOptions().size() != 0) {
				
				for (OrderDishOptionDto orderDishOption : dto.getOrderDishOptions()) {								// n???u m?? c?? topping th?? ho???c l?? update topping, ho???c l?? th??m topping m???i
					if(orderDishOption.getOrderDishOptionId() == 999999999 && orderDishOption.getQuantity() > 0) {		// n???u id g???i v??? l?? 99999999 v?? quantity > 0 th?? l?? insert m???i
						orderDishOptionService.insertOrderDishOption(orderDishOption, dto.getOrderDishId());
					}else if(orderDishOption.getOrderDishOptionId() == 999999999){										// n???u id g???i v??? l?? 99999999 th?? ko l??m g?? c???
					}else {																								// n???u id g???i v??? ko ph???i l?? 99999999, t???c l?? update c??i topping c??
						if(orderDishOption.getQuantity() == 0) {														// n???u cho v??? quantity = 0 th?? x??a n?? ??i
							orderDishOptionRepo.deleteOrderDishOptionById(orderDishOption.getOrderDishOptionId());
						}else {																							// n???u ko th?? update
							orderDishOptionService.updateQuantityOrderDishOption(orderDishOption);
						}
					}
				}
				result = orderDishRepo.updateToppingComment(dto.getComment(), dto.getSellPrice(), dto.getSumPrice(), dto.getOrderDishId());	// xong th?? c???p nh???t l???i comment v?? gi??
				if(result != 0) { 																						// c???p nh???t l???i order
					SumQuantityAndPriceDto sum = getSumQtyAndPriceByOrder(dto.getOrderOrderId());
					result = orderService.updateOrderQuantity(sum.getSumQuantity(), sum.getSumPrice(), dto.getOrderOrderId());
				}
			}else if(dto!= null && dto.getOrderDishId() != null && dto.getOrderDishOptions().size() == 0) {												// n???u ko g???i topping v??? th?? l?? ch??? comment
				result = orderDishRepo.updateComment(dto.getComment(), dto.getOrderDishId());
			}
			simpMessagingTemplate.convertAndSend("/topic/chef", orderService.getListDisplayChefScreen());
			simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
			simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderOrderId(), orderService.getOrderDetailById(dto.getOrderOrderId()));
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return result;
	}

	/**
	 * select by id
	 */
	@Override
	public OrderDishDto getOrderDishById(Long orderDishId) {
		OrderDishDto dto = null;
		try {
			if(orderDishId != null) {
				OrderDish entity = orderDishRepo.findOrderDishById(orderDishId);
				if(entity != null) {
					dto = orderDishMapper.entityToDto(entity);
				}
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return dto;
	}

	/**
	 * thay ?????i n???u cancel m??n trong order
	 */
	@Override
	@Transactional
	public String updateCancelOrderDish(OrderDishDto dto) {
		try {
			if(dto.getOrderDishId() != null && dto.getOrderOrderId() != null) {
				
				OrderDish orderDish=orderDishRepo.findById(dto.getOrderDishId()).get();
				OrderDishCancelDto orderDishCancelDto = new OrderDishCancelDto();
				if(orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_ORDERED
						|| orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_CANCELED) {
					return AppMessageErrorConstant.STATUS_NOT_CHANGE;
				}
				if(orderDish.getQuantityOk() == null) {	
					return AppMessageErrorConstant.NO_DATA;
				}else {
					if(orderDish.getQuantityOk() != orderDish.getQuantity()) {									// l???n th??? 2,3.. h???y m??n
						if(dto.getQuantityCancel() == orderDish.getQuantityOk()) {								// h???y h???t
							if(orderDish.getOrderDishOptions().size() != 0) {
								orderDishOptionRepo.updateCancel(StatusConstant.STATUS_ORDER_DISH_OPTION_CANCELED, dto.getOrderDishId());
							}
							orderDishCancelDto = new OrderDishCancelDto(null, dto.getQuantityCancel(), dto.getCommentCancel(), dto.getModifiedBy(), Utils.getCurrentTime(), dto.getOrderDishId());
							try {
								orderDishCancelService.insertCancel(orderDishCancelDto);						// thay ?????i th?? th??m b???n ghi v??o b???ng cancel
							} catch (Exception e) {
								return AppMessageErrorConstant.STATUS_NOT_CHANGE;
							}
							dto.setQuantityOk(0);
							dto.setQuantityCancel(orderDish.getQuantity()); 									// h???y h???t r???i th?? = s??? l?????ng quantity ban ?????u
							dto.setSumPrice(dto.getQuantityOk()*orderDish.getSellPrice());						// set l???i t???ng gi?? = 0
							
						}else if(dto.getQuantityCancel() < orderDish.getQuantityOk()) {							// h???y th??m 1 s???
							orderDishCancelDto = new OrderDishCancelDto(null, dto.getQuantityCancel(), dto.getCommentCancel(), dto.getModifiedBy(), Utils.getCurrentTime(), dto.getOrderDishId());
							try {
								orderDishCancelService.insertCancel(orderDishCancelDto);						// thay ?????i th?? th??m b???n ghi v??o b???ng cancel
							} catch (Exception e) {
								return AppMessageErrorConstant.STATUS_NOT_CHANGE;
							}
							dto.setQuantityOk(orderDish.getQuantityOk() - dto.getQuantityCancel());
							dto.setQuantityCancel(orderDish.getQuantityCancel() + dto.getQuantityCancel());
							dto.setSumPrice(dto.getQuantityOk()*orderDish.getSellPrice());
						}else {
							return AppMessageErrorConstant.CANCEL_NOT_MORE_THAN_OK;
						}
					}else {																						// l???n ?????u h???y m??n	
						if(dto.getQuantityCancel() == orderDish.getQuantityOk()) {								// h???y h???t
							if(orderDish.getOrderDishOptions().size() != 0) {
								orderDishOptionRepo.updateCancel(StatusConstant.STATUS_ORDER_DISH_OPTION_CANCELED, dto.getOrderDishId());
							}
							orderDishCancelDto = new OrderDishCancelDto(null, dto.getQuantityCancel(), dto.getCommentCancel(), dto.getModifiedBy(), Utils.getCurrentTime(), dto.getOrderDishId());
							try {
								orderDishCancelService.insertCancel(orderDishCancelDto);						// thay ?????i th?? th??m b???n ghi v??o b???ng cancel
							} catch (Exception e) {
								return AppMessageErrorConstant.STATUS_NOT_CHANGE;
							}
							dto.setQuantityOk(0);
							dto.setQuantityCancel(orderDish.getQuantity());										// t???ng s??? quantity g???i ban ?????u
							dto.setSumPrice(dto.getQuantityOk()*orderDish.getSellPrice());						// t??nh l???i t???ng gi??	
						}else if(dto.getQuantityCancel() < orderDish.getQuantityOk()) {							// h???y 1 s???
							orderDishCancelDto = new OrderDishCancelDto(null, dto.getQuantityCancel(), dto.getCommentCancel(), dto.getModifiedBy(), Utils.getCurrentTime(), dto.getOrderDishId());
							try {
								orderDishCancelService.insertCancel(orderDishCancelDto);						// thay ?????i th?? th??m b???n ghi v??o b???ng cancel
							} catch (Exception e) {
								return AppMessageErrorConstant.STATUS_NOT_CHANGE;
							}
							dto.setQuantityOk(orderDish.getQuantityOk() - dto.getQuantityCancel());
							dto.setQuantityCancel(dto.getQuantityCancel() + orderDish.getQuantityCancel());
							dto.setSumPrice(dto.getQuantityOk()*orderDish.getSellPrice());
						}else {
							return AppMessageErrorConstant.CANCEL_NOT_MORE_THAN_OK;
						}
					}
				}
				orderDish.setQuantityOk(dto.getQuantityOk());
				orderDish.setSumPrice(dto.getSumPrice());
				orderDish.setQuantityCancel(dto.getQuantityCancel());
				orderDish.setModifiedBy(dto.getModifiedBy());
				orderDish.setModifiedDate(Utils.getCurrentTime());
				if(dto.getQuantityCancel() == orderDish.getQuantity()) {
					Status status = statusRepo.findById(StatusConstant.STATUS_ORDER_DISH_CANCELED)
							.orElseThrow(()-> new NotFoundException("Not found Status: "+StatusConstant.STATUS_ORDER_DISH_CANCELED));
					orderDish.setStatus(status);
					orderDish.setCommentCancel(dto.getCommentCancel());
				}
				orderDishRepo.save(orderDish);
				
				SumQuantityAndPriceDto sum = getSumQtyAndPriceByOrder(dto.getOrderOrderId());								// c???p nh???t l???i s??? l?????ng v?? gi?? trong order
				orderService.updateOrderQuantity(sum.getSumQuantity(), sum.getSumPrice(), dto.getOrderOrderId());
				simpMessagingTemplate.convertAndSend("/topic/chef", orderService.getListDisplayChefScreen());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderOrderId(), orderService.getOrderDetailById(dto.getOrderOrderId()));		// socket
				
			}
			
		} catch (NullPointerException e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return AppMessageErrorConstant.CHANGE_SUCCESS;
	}
	
	/**
	 * hi???n th??? danh s??ch l??n m??n h??nh tr??? m??n, ch??? hi???n th??? c??c m??n ???? ho??n th??nh
	 */
	@Override
	public List<OrderDishDto> getCanReturnByOrderId(Long orderId) {
		
		List<OrderDishDto> listDto = null;
		try {
			if(orderId != null) {
				List<OrderDish> listOrderDish = orderDishRepo.findCanReturnByOrderId(StatusConstant.STATUS_ORDER_DISH_COMPLETED, orderId);
				listDto = listOrderDish.stream().map(orderDishMapper::entityToDto)
						.collect(Collectors.toList());	
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return listDto;
	}

	@Override
	@Transactional
	public String updateReturnDish(List<OrderDishRequest> listOdr) {
		
		try {
			Export export = null;
			Map<Long, Double> map = new HashMap<Long, Double>();
			boolean checkReturnOk;
			boolean checkMaterial=false;
			DishInOrderDishDto dishIn = null;
			Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> mapDish = new HashMap<DishInOrderDishDto, List<GetQuantifierMaterialDto>>();
			List<GetQuantifierMaterialDto> listQuantifier = null;
			if(listOdr != null && listOdr.size() != 0) {
				OrderDish orderDish = null;
				for (OrderDishRequest orderDishRequest : listOdr) {													// duy???t list tr??? m??n
					checkReturnOk = false;
					if(orderDishRequest.getQuantityReturn() > 0) {													// m??n ???? s??? l?????ng tr??? ph???i > 0									
						orderDish = new OrderDish();
						orderDish = orderDishRepo.findById(orderDishRequest.getOrderDishId()).get();				// t??m l???i m??n ????
						if(orderDish.getQuantityOk() >= orderDishRequest.getQuantityReturn()) {									// s??? l?????ng tr??? ph???i nh??? h??n s??? l?????ng th???c t??? g???i
							orderDish.setQuantity(orderDish.getQuantity()-orderDishRequest.getQuantityReturn());
							orderDish.setQuantityOk(orderDish.getQuantityOk()-orderDishRequest.getQuantityReturn());
							orderDish.setModifiedBy(orderDishRequest.getModifiedBy());
							orderDish.setModifiedDate(Utils.getCurrentTime());
							orderDish.setSumPrice(orderDish.getQuantityOk()*orderDish.getSellPrice());
							orderDishRepo.save(orderDish);
							checkReturnOk=true;																		// m??n n??y c?? tr??? l???i
						}else {
							return AppMessageErrorConstant.QUANTITY_RETURN;
						}
					}
					if(checkReturnOk) {																				// m??n n??y c?? tr??? m??n
						dishIn = new DishInOrderDishDto();
						dishIn.setDishId(orderDish.getDish().getDishId());
						dishIn.setQuantity(orderDishRequest.getQuantityReturn());
						dishIn.setOrderDishId(orderDishRequest.getOrderDishId());
						listQuantifier = new ArrayList<GetQuantifierMaterialDto>();
						listQuantifier = orderRepo.findListQuantifierMaterialByDish(dishIn.getDishId());
						if(listQuantifier.size()!= 0) {
							mapDish.put(dishIn, listQuantifier);													// l??u v??o map: dish v?? list material theo dish
							checkMaterial = true;
						}	
					}
				}
				if(checkMaterial) {
					map = CheckMaterialUtils.testKho(mapDish);															// ph??n t??ch ra theo material v?? quantity
					Long exportId = exportRepo.findByOrderId(listOdr.get(0).getOrderId());							// l???y ra export id theo order id
					if(exportId != null) {
						export = exportRepo.findById(exportId).orElseThrow(
								() -> new NotFoundException("Not found Export: " + exportId));
						Material material = null;
						List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();
						Double remainNew = 0d, totalExportNew = 0d, quantityExportNew = 0d;
						for (Long materialId : map.keySet()) {
							for (ExportMaterial exportMaterial : export.getExportMaterials()) {
								if(materialId == exportMaterial.getMaterial().getMaterialId()) {						// t??m material li??n quan ?????n m??n ??n ????
									material = exportMaterial.getMaterial();											// l???y ra material ????
									
									remainNew = Utils.sumBigDecimalToDouble(material.getRemain(), map.get(materialId));			// remain c??n l???i: tr??? ??i s??? l?????ng export
									totalExportNew = Utils.subtractBigDecimalToDouble(material.getTotalExport(), map.get(materialId));		// t??ng l??n s??? l?????ng export
									quantityExportNew = Utils.subtractBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update l???i s??? l?????ng export
										
									material.setTotalExport(totalExportNew);
									material.setRemain(remainNew);
									exportMaterial.setMaterial(material);
									exportMaterial.setQuantityExport(quantityExportNew);
									exportMaterials.add(exportMaterial);												// l??u l???i v??o list
									break;
								}
							}
						}
						Iterator<ExportMaterial> exportIte = export.getExportMaterials().iterator();					// tr??? ??i th???ng n??o ???? c?? material trong export tr?????c ????
						while (exportIte.hasNext()) {
							Long materialId = exportIte.next().getMaterial().getMaterialId();
							for (ExportMaterial exportMaterial : exportMaterials) {
								if(materialId == exportMaterial.getMaterial().getMaterialId()) {
									exportIte.remove();																	// t??m ???????c th???ng n??o ???? c?? tr?????c ???? th?? x??a
									break;
								}
							}
						}
						export.getExportMaterials().addAll(exportMaterials);
//						export.setExportMaterials(exportMaterials);													// l??u l???i v??o export
						exportRepo.save(export);		
					}
																				// l??u v??o database
				}
				
				Long orderId = listOdr.get(0).getOrderId();
				if(orderId != null) {
					SumQuantityAndPriceDto sum = getSumQtyAndPriceByOrder(orderId);									// c???p nh???t l???i s??? l?????ng v?? gi?? trong order
					orderService.updateOrderQuantity(sum.getSumQuantity(), sum.getSumPrice(), orderId);
					simpMessagingTemplate.convertAndSend("/topic/orderdetail/" + orderId, orderService.getOrderDetailById(orderId));		// socket
				}
			}
			
		}catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return AppMessageErrorConstant.CHANGE_SUCCESS;
	}

	/*
	 * ?????i t???t c??? order theo m??n
	 */
	@Override
	@Transactional
	public String updateStatusByDish(OrderDishChefRequest request) {
		int result = 0;
		String message = "";
		try {
//			Long statusCurrent = orderDishRepo.getStatusByOrderDishId(request.getOrderDishId());										// t??m tr???ng th??i hi???n t???i c???a m??n
			if(request.getStatusId() != null && request.getDishId() != null) {
				message = "??ang th???c hi???n: " + request.getDishName();
				int count = 0;
				if(request.getStatusId() == StatusConstant.STATUS_ORDER_DISH_PREPARATION) {												// b???m x??c nh???n th???c hi???n
					result = orderDishRepo.updateStatusByDish(StatusConstant.STATUS_ORDER_DISH_PREPARATION, request.getDishId());
					if(result != 0) {
						List<Long> listOrderId = orderDishRepo.findOrderIdByDishId(StatusConstant.STATUS_ORDER_ORDERED, request.getDishId());
						for (Long orderId : listOrderId) {																				// t??m c??c m??n li??n quan dishid ????, 
							count = orderDishRepo.findCountStatusOrdered(orderId, StatusConstant.STATUS_ORDER_DISH_ORDERED);			//????? chuy???n tr???ng th??i c??? order n???u ko c??n m??n n??o
							if(count == 0) {
								result = orderRepo.updateOrderChef(request.getChefStaffId(), StatusConstant.STATUS_ORDER_PREPARATION, orderId);
								simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+orderId, orderService.getOrderDetailById(orderId));		// socket
							}
						}
					}
				}else if(request.getStatusId() == StatusConstant.STATUS_ORDER_DISH_COMPLETED) {											// b???m ???? ho??n th??nh m??n ????
					result = orderDishRepo.updateStatusByDish(StatusConstant.STATUS_ORDER_DISH_COMPLETED, request.getDishId());
					message = "???? ho??n th??nh: " + request.getDishName();
					if(result != 0) {
						List<Long> listOrderId = orderDishRepo.findOrderIdByDishId(StatusConstant.STATUS_ORDER_PREPARATION, request.getDishId());
						for (Long orderId : listOrderId) {																				// t??m c??c m??n li??n quan dishid ????, 
							count = orderDishRepo.findCountStatusPrepareAndOrdered(orderId, StatusConstant.STATUS_ORDER_DISH_ORDERED, StatusConstant.STATUS_ORDER_DISH_PREPARATION);		//????? chuy???n tr???ng th??i c??? order n???u ko c??n m??n n??o
							if(count == 0) {
								result = orderRepo.updateOrderChef(request.getChefStaffId(), StatusConstant.STATUS_ORDER_COMPLETED, orderId);
								simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+orderId, orderService.getOrderDetailById(orderId));		// socket
							}
						}
					}
				}
				
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/chef", orderService.getListDisplayChefScreen());
			}
			
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return message;
	}

	/*
	 * ?????i theo t???ng order, m??n
	 */
	@Override
	@Transactional
	public OrderDishChefDto updateStatusByDishAndOrder(OrderDishChefRequest request) {
		OrderDishChefDto orderdishChef = null;
		int result = 0;
		try {
			if(request.getOrderDishId() != null) {
				int count = 0;
				Long orderId = orderDishRepo.findOrderByOrderDishId(request.getOrderDishId());
				if(request.getStatusId() != null) {
					if(request.getStatusId() == StatusConstant.STATUS_ORDER_DISH_PREPARATION) {											// x??c nh???n th???c hi???n m??n ??n
						result = orderDishRepo.updateStatusByDishAndOrder(StatusConstant.STATUS_ORDER_DISH_PREPARATION, request.getOrderDishId());
						count = orderDishRepo.findCountStatusOrdered(orderId, StatusConstant.STATUS_ORDER_DISH_ORDERED);				// ko c??n th???ng n??o ordered th?? chuy???n sang preparation
						if(count == 0) {
							result = orderRepo.updateOrderChef(request.getChefStaffId(), StatusConstant.STATUS_ORDER_PREPARATION, orderId);
						}
					}else if(request.getStatusId() == StatusConstant.STATUS_ORDER_DISH_COMPLETED) {										// x??c nh???n th???c hi???n m??n ??n xong
						result = orderDishRepo.updateStatusByDishAndOrder(StatusConstant.STATUS_ORDER_DISH_COMPLETED, request.getOrderDishId());
						count = orderDishRepo.findCountStatusPrepareAndOrdered(orderId, StatusConstant.STATUS_ORDER_DISH_ORDERED, StatusConstant.STATUS_ORDER_DISH_PREPARATION);
						if(count == 0) {																								// ko c??n ordered v?? prepare th?? sang completed
							result = orderRepo.updateOrderChef(request.getChefStaffId(), StatusConstant.STATUS_ORDER_COMPLETED, orderId);
						}
					}
				}
				if(count==0) {
					simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				}
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+orderId, orderService.getOrderDetailById(orderId));		// socket
				simpMessagingTemplate.convertAndSend("/topic/chef", orderService.getListDisplayChefScreen());
			}
			if(result != 0) {																				// upadate th??nh c??ng
				OrderDish entity = orderDishRepo.findById(request.getOrderDishId())
						.orElseThrow(()-> new NotFoundException("Not found OrderDish: " + request.getOrderDishId()));
				orderdishChef = orderDishMapper.entityToChef(entity);	
			}
			
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return orderdishChef;
	}


}
