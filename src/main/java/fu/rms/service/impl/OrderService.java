package fu.rms.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fu.rms.entity.Status;
import fu.rms.constant.AppMessageErrorConstant;
import fu.rms.constant.StatusConstant;
import fu.rms.dto.DishInOrderDishDto;
import fu.rms.dto.GetQuantifierMaterialDto;
import fu.rms.dto.OrderChefDto;
import fu.rms.dto.OrderDetailDto;
import fu.rms.dto.OrderDishDto;
import fu.rms.dto.OrderDishOptionDto;
import fu.rms.dto.OrderDto;
import fu.rms.dto.RemainDto;
import fu.rms.dto.ReportDishTrendDto;
import fu.rms.entity.Export;
import fu.rms.entity.ExportMaterial;
import fu.rms.entity.Material;
import fu.rms.entity.Order;
import fu.rms.entity.OrderDish;
import fu.rms.entity.Tables;
import fu.rms.exception.NotFoundException;
import fu.rms.mapper.OrderMapper;
import fu.rms.repository.ExportRepository;
import fu.rms.repository.MaterialRepository;
import fu.rms.repository.OrderDishOptionRepository;
import fu.rms.repository.OrderDishRepository;
import fu.rms.repository.OrderRepository;
import fu.rms.repository.StatusRepository;
import fu.rms.repository.TableRepository;
import fu.rms.request.OrderRequest;
import fu.rms.service.IOrderService;
import fu.rms.utils.CheckMaterialUtils;
import fu.rms.utils.Utils;
import fu.rms.exception.NullPointerException;

@Service
public class OrderService implements IOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private TableService tableService;
	
	@Autowired
	private TableRepository tableRepo;
	
	@Autowired
	private StatusRepository statusRepo;
	
	@Autowired
	private OrderDishService orderDishService;
	
	@Autowired
	private OrderDishOptionService orderDishOptionService;
	
	@Autowired
	private OrderDishRepository orderDishRepo;
	
	@Autowired
	private OrderDishOptionRepository orderDishOptionRepo;
	
	@Autowired
	private MaterialRepository materialRepo;
	
	@Autowired
	private ExportRepository exportRepo;
	
	@Autowired
	private ReportDishTrendService reportService;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	/**
	 * t???o m???i order
	 */
	@Override
	@Transactional
	public OrderDto insertOrder(OrderDto dto) {
		
		String orderCode = Utils.generateOrderCode();
		OrderDto orderDto = null;
		int result=0;
		try {
			if(dto != null && dto.getTableId() != null && dto.getOrderTakerStaffId() != null) {
				result = orderRepo.insertOrder(dto.getOrderTakerStaffId(), dto.getTableId(), StatusConstant.STATUS_ORDER_ORDERING, 
						orderCode, dto.getCreateBy());
				if(result != 0) {
					orderDto = getOrderByCode(orderCode);
					tableService.updateTableNewOrder(orderDto.getOrderId(), orderDto.getTableId(), StatusConstant.STATUS_TABLE_BUSY);
					//notify for client when update table
					simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				}
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}

		return orderDto;
	}

	@Override
	public OrderDto getOrderByCode(String orderCode) {
		OrderDto dto = new OrderDto();
		try {
			if(orderCode != null) {
				Order entity = orderRepo.findOrderByCode(orderCode);
				dto.setOrderId(entity.getOrderId());
				dto.setOrderTakerStaffId(entity.getOrderTakerStaff().getStaffId());
				dto.setTableId(entity.getTable().getTableId());
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return dto;
	}

	/**
	 * Khi order xong: save order
	 */
	@Override
	@Transactional
	public OrderDetailDto updateSaveOrder(OrderDto dto) {

		OrderDetailDto orderDetail = null;
		Map<Long, Double> map = null;
		Map<Long, Double> map2 = new HashMap<Long, Double>();
		Long statusOrder = null;
		boolean check = false;
		boolean checkEmptyMaterial = true;
		
		if(dto != null && dto.getOrderId() != null) {
			try {
				if(dto.getOrderDish() == null || dto.getOrderDish().size() == 0 ) {
				}else {
					List<DishInOrderDishDto> listDish = new ArrayList<DishInOrderDishDto>();							// xu ly check kho
					DishInOrderDishDto dish = null;
					for (OrderDishDto orderDishDto : dto.getOrderDish()) {										// l???y c??c dish id v?? quantity
						dish = new DishInOrderDishDto();
						dish.setOrderDishId(orderDishDto.getOrderDishId());
						dish.setDishId(orderDishDto.getDish().getDishId());
						dish.setQuantity(orderDishDto.getQuantity());
						listDish.add(dish);
					}
					
					List<GetQuantifierMaterialDto> listQuantifier = null;
					List<GetQuantifierMaterialDto> listQuantifiers = new ArrayList<GetQuantifierMaterialDto>();
					Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> mapDish = new HashMap<DishInOrderDishDto, List<GetQuantifierMaterialDto>>();
					for (DishInOrderDishDto dishIn : listDish) {													//m???i dish s??? t????ng ???ng v???i 1 list c??c quantifiers
						listQuantifier = new ArrayList<GetQuantifierMaterialDto>();
						listQuantifier = orderRepo.findListQuantifierMaterialByDish(dishIn.getDishId());
						if(listQuantifier.size()!= 0) {
							listQuantifiers.addAll(listQuantifier);													// add vao list tong
							mapDish.put(dishIn, listQuantifier);
						}
					}
					Set<String> setMaterialName = new HashSet<String>();
					if(!mapDish.isEmpty()) {																		// map ph???i c?? ph???n t???
						checkEmptyMaterial = false;
						map = CheckMaterialUtils.testKho(mapDish);														// x??? l?? ra th??nh c??c nguy??n v???t li???u
						Set<Long> listDishId = new LinkedHashSet<Long>();
						for (Long materialId : map.keySet()) {
							RemainDto remain = materialRepo.findRemainById(materialId);
							Double remainMaterial = remain.getRemain();
							if(map.get(materialId) > remainMaterial) {												// neu nvl can > nvl con lai
								for (GetQuantifierMaterialDto getQuantifierMaterial : listQuantifiers) {
									if(materialId == getQuantifierMaterial.getMaterialId()) {						//tim kiem cac dish co material thieu
										listDishId.add(getQuantifierMaterial.getDishId());							//luu lai dish id trung su dung set ko luu cac id trung
//										map2.put(materialId, map.get(materialId));									// l??u l???i material thi???u
										String materialName = remain.getMaterialName();
										setMaterialName.add(materialName);
									}
								}
								check = true;																		// co nvl ko du
							}
						}
						
						if(check) {																					// co dish ko du
							List<OrderDishDto> listOrderDish = new ArrayList<OrderDishDto>();
							
							///////////////////////////////////////////////////////////////////////////////////////////////////start
							List<String> listMessage = new ArrayList<String>();
							int max=0;
							Map<Long, Integer> mapNumber = new HashMap<Long, Integer>();
							List<GetQuantifierMaterialDto> listQuantifierCheck = null;
							Map<Long, List<GetQuantifierMaterialDto>> mapDish2 = null;
							for (Long dishId : listDishId) {														//c??c m??n ??n ko ????? nvl
								boolean checkMax = false;
								listQuantifierCheck = new ArrayList<GetQuantifierMaterialDto>();
								listQuantifierCheck = orderRepo.findListQuantifierMaterialByDish(dishId);
								mapDish2 = new HashMap<Long, List<GetQuantifierMaterialDto>>();
								map2 = new HashMap<Long, Double>();
								mapDish2.put(dishId, listQuantifierCheck);
								map2 = CheckMaterialUtils.calculateMaterial(mapDish2);
								if(listQuantifierCheck.size()!= 0) {
									max=0;
									for (Long materialId : map.keySet()) {											// map ch???a c??c material t???t c???
										RemainDto remain = materialRepo.findRemainById(materialId);
										Double remainMaterial = remain.getRemain();
//										if(map.get(materialId) > remainMaterial) {
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
//										}
									}
									mapNumber.put(dishId, max);
								}	
							}
							String textMes="";
							for (Long dishId : mapNumber.keySet()) {
								textMes="";
								for (OrderDishDto orderDish : dto.getOrderDish()) {									// tim lai trong cac mon da order
									if(dishId.equals(orderDish.getDish().getDishId())) {
//										if(mapNumber.get(dishId) < orderDish.getQuantity()) {						// ch??? hi???n th??? th???ng n??o s??? nvl ko ????? cho s??? l?????ng ????
											listOrderDish.add(orderDish);	
											textMes = orderDish.getDish().getDishName() + ": "+ mapNumber.get(dishId) + " " +  orderDish.getDish().getDishUnit() + " \n";
											listMessage.add(textMes);
											break;
//										}
									}
								}
							}

							/////////////////////////////////////////////////////////////////////////////////////////////////////////end
							orderDetail = new OrderDetailDto();
							orderDetail = orderMapper.dtoToDetail(dto);
							orderDetail.setOrderDish(listOrderDish);
							orderDetail.setMessageMaterial(setMaterialName);
							orderDetail.setMessage(listMessage);
							return orderDetail;																		//tra ve order
						}
					}

					for (OrderDishDto orderDish : dto.getOrderDish()) {
						Long orderDishId = orderDishService.insertOrderDish(orderDish, dto.getOrderId());
						if(orderDish.getOrderDishOptions() == null || orderDish.getOrderDishOptions().size() == 0) {
						}else{
							for (OrderDishOptionDto orderDishOption : orderDish.getOrderDishOptions()) {
								orderDishOptionService.insertOrderDishOption(orderDishOption, orderDishId);
							}
						}
					}
					statusOrder = orderRepo.findStatusOrderById(dto.getOrderId());
					if(statusOrder == StatusConstant.STATUS_ORDER_ORDERING) {										// ch??a order th?? update tr???ng th??i, ng??y order
						Date orderDate = Utils.getCurrentTime();
						orderRepo.updateSaveOrder(StatusConstant.STATUS_ORDER_ORDERED, orderDate, dto.getTotalItem(), 
								dto.getTotalAmount(), dto.getComment(), dto.getOrderId());
					} else { 																							// n???u ???? order r???i th?? ch??? update s??? l?????ng v?? gi??
						orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_ORDERED, dto.getOrderId());
						updateOrderQuantity(dto.getTotalItem(), dto.getTotalAmount(), dto.getOrderId());
					}
					tableService.updateStatusOrdered(dto.getTableId(), StatusConstant.STATUS_TABLE_ORDERED);
				}
			} catch (NullPointerException e) {
				throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
			} catch (Exception e) {
				throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
			}
		
			try {
				if(!checkEmptyMaterial) {														// c?? nguy??n li???u
					//export here																// t???o m???i export
					if(statusOrder == StatusConstant.STATUS_ORDER_ORDERING) {					// l???n ?????u order
						Export export = new Export();
						export.setComment(null);
						export.setExportCode(Utils.generateExportCode());
						Order order = orderRepo.findById(dto.getOrderId()).orElseThrow(
								() -> new NotFoundException("Not found order: " + dto.getOrderCode()));
						export.setOrder(order);
						List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();
						ExportMaterial exportMaterial = null;
						Material material = null;
						for (Long materialId : map.keySet()) {
							
							Double remainNew = 0d, totalExportNew = 0d;
							exportMaterial = new ExportMaterial();
							material = materialRepo.findById(materialId).orElseThrow(
									() -> new NotFoundException("Not found MaterialId: " + materialId));
							exportMaterial.setQuantityExport(Utils.bigDecimalToDouble(map.get(materialId)));											// l???y t???ng s??? l?????ng material
							exportMaterial.setUnitPrice(material.getUnitPrice());
							
							remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));		// remain c??n l???i: tr??? ??i s??? l?????ng export
							totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));	// t??ng l??n s??? l?????ng export			
							
							material.setTotalExport(totalExportNew);
							material.setRemain(remainNew);
							exportMaterial.setMaterial(material);
							exportMaterial.setExport(export);
							exportMaterials.add(exportMaterial);

						}
						export.setExportMaterials(exportMaterials);
						exportRepo.save(export);															// save export
						//export end
					}else {
						// s???a l???i export tr?????c ????
						try {
							
							Export export = null;																			// t??ng s??? l?????ng
							Long exportId = exportRepo.findByOrderId(dto.getOrderId());										// l???y ra export id theo order id
							if(exportId != null) {
								export = exportRepo.findById(exportId).orElseThrow(
										() -> new NotFoundException("Not found Export: " + exportId));
								
								List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();		
								Material material = null;
								Double remainNew = 0d, totalExportNew = 0d, quantityExportNew = 0d;
								boolean checkMaterial=false;
								ExportMaterial exportMaterialNew;
								for (Long materialId : map.keySet()) {															// upadate l???i material, exportmaterial
									checkMaterial=false;
									for (ExportMaterial exportMaterial : export.getExportMaterials()) {
										if(materialId == exportMaterial.getMaterial().getMaterialId()) {						// t??m material li??n quan ?????n m??n ??n ????
											material = exportMaterial.getMaterial();											// l???y ra material ????
											
											remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));			// remain c??n l???i: tr??? ??i s??? l?????ng export
											totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));		// t??ng l??n s??? l?????ng export
											quantityExportNew = Utils.sumBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update l???i s??? l?????ng export
											
											material.setTotalExport(totalExportNew);
											material.setRemain(remainNew);
											exportMaterial.setMaterial(material);
											exportMaterial.setQuantityExport(quantityExportNew);
											exportMaterials.add(exportMaterial);												// l??u l???i v??o list
											checkMaterial=true;																	//nvl n??y ???? c?? trong l???n export tr?????c ????
										}
									}
									if(!checkMaterial) {																		// nvl n??y ch??a c?? trong l???n export tr?????c ????, th?? s??? add th??m																		
										remainNew = 0d;
										totalExportNew = 0d;
										exportMaterialNew = new ExportMaterial();
										material = materialRepo.findById(materialId).orElseThrow(								// select l???i material ????
												() -> new NotFoundException("Not found MaterialId: " + materialId));
										exportMaterialNew.setQuantityExport(Utils.bigDecimalToDouble(map.get(materialId)));		// l???y t???ng s??? l?????ng material
										exportMaterialNew.setUnitPrice(material.getUnitPrice());
										
										remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));		// remain c??n l???i: tr??? ??i s??? l?????ng export
										totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));	// t??ng l??n s??? l?????ng export
										
										material.setTotalExport(totalExportNew);
										material.setRemain(remainNew);
										
										exportMaterialNew.setMaterial(material);
										exportMaterialNew.setExport(export);
										exportMaterials.add(exportMaterialNew);
										
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
								export.getExportMaterials().addAll(exportMaterials);											// add th??m th???ng n??o ch??a c?? material trong export tr?????c ????
								exportRepo.save(export);																		// l??u v??o database
								// end s???a export
							} else {
								export = new Export();
								export.setComment(null);
								export.setExportCode(Utils.generateExportCode());
								Order order = orderRepo.findById(dto.getOrderId()).orElseThrow(
										() -> new NotFoundException("Not found order: " + dto.getOrderCode()));
								export.setOrder(order);
								List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();
								ExportMaterial exportMaterial = null;
								Material material = null;
								for (Long materialId : map.keySet()) {
									
									Double remainNew = 0d, totalExportNew = 0d;
									exportMaterial = new ExportMaterial();
									material = materialRepo.findById(materialId).orElseThrow(
											() -> new NotFoundException("Not found MaterialId: " + materialId));
									exportMaterial.setQuantityExport(Utils.bigDecimalToDouble(map.get(materialId)));											// l???y t???ng s??? l?????ng material
									exportMaterial.setUnitPrice(material.getUnitPrice());
									
									remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));		// remain c??n l???i: tr??? ??i s??? l?????ng export
									totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));	// t??ng l??n s??? l?????ng export

									material.setTotalExport(totalExportNew);
									material.setRemain(remainNew);
									exportMaterial.setMaterial(material);
									exportMaterial.setExport(export);
									exportMaterials.add(exportMaterial);

								}
								export.setExportMaterials(exportMaterials);
								exportRepo.save(export);
							}
						} catch (Exception e) {
							throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
						}
					}
				}
				
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/chef", getListDisplayChefScreen());
				orderDetail = getOrderDetailById(dto.getOrderId());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), orderDetail);		// socket
		
			} catch(Exception e) {
				return orderDetail = new OrderDetailDto();
			}
			
		}
		return orderDetail;
	}
	
	/**
	 * thay ?????i b??n
	 */
	@Override
	@Transactional
	public String changeOrderTable(OrderDto dto, Long tableId) {
		
		String result = "";
		int update = 0;
		try {
			if(dto != null && dto.getOrderId() != null && tableId != null) {
				Long statusTable = tableRepo.findStatusByTableId(tableId);
				if(statusTable == StatusConstant.STATUS_TABLE_ORDERED) {										// b??n ??ang b???n th?? ko ?????i ???????c
					return AppMessageErrorConstant.TABLE_ORDERED;
				}else if(statusTable == StatusConstant.STATUS_TABLE_BUSY) {										// b??n ??ang b???n th?? ko ?????i ???????c
					return AppMessageErrorConstant.TABLE_BUSY;
				}else {
					if(dto.getTableId() != null) {
						tableRepo.updateToReady(dto.getTableId(), StatusConstant.STATUS_TABLE_READY); 			// ?????i b??n c?? th??nh tr???ng th??i ready
						if(dto.getStatusId() == StatusConstant.STATUS_ORDER_ORDERING) {
							tableService.updateTableNewOrder(dto.getOrderId(), tableId, StatusConstant.STATUS_TABLE_BUSY);				// ?????i b??n m???i th??nh tr???ng th??i theo order ?????i
						}else {
							tableService.updateTableNewOrder(dto.getOrderId(), tableId, StatusConstant.STATUS_TABLE_ORDERED);
						}
						update = orderRepo.updateOrderTable(dto.getTableId(), dto.getModifiedBy(), Utils.getCurrentTime(), dto.getOrderId());
					}
				}
				if (update != 0) {
					result = AppMessageErrorConstant.CHANGE_SUCCESS;
				}else {
					result = AppMessageErrorConstant.TABLE_ERROR;
				}
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/chef", getListDisplayChefScreen());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return result;
	}

	/**
	 * h???y order
	 */
	@Override
	@Transactional
	public int updateCancelOrder(OrderDto dto) {
		int result = 0;
		if(dto != null && dto.getOrderId() != null && dto.getStatusId() != null) {
			try {
				if(dto.getStatusId() == StatusConstant.STATUS_ORDER_ORDERING) { 									// m???i t???o order, ch??a ch???n m??n
					try {
						tableRepo.updateToReady(dto.getTableId(), StatusConstant.STATUS_TABLE_READY);
						result = orderRepo.updateCancelOrder(StatusConstant.STATUS_ORDER_CANCELED, Utils.getCurrentTime(), dto.getModifiedBy(), dto.getComment(), dto.getOrderId());
					} catch (Exception e) {
						return AppMessageErrorConstant.RETURN_ERROR_NULL;
					}	
				}else if(dto.getStatusId() == StatusConstant.STATUS_ORDER_ORDERED) {								// m???i order th?? c?? th??? h???y order, back l???i nvl
					List<OrderDish> listOrderDish = orderDishRepo.findDishOrderedByOrder(dto.getOrderId());
//					
//					if(listOrderDish.size() != 0) {	
//						for (OrderDish orderDish : listOrderDish) {
//							if(orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_PREPARATION){
//								
//								
//							}
//						}
//					}
//					
//					
//					
//					
//					
//					
					if(listOrderDish.size() != 0) {	
						Map<Long, Double> map = new HashMap<Long, Double>();
						List<DishInOrderDishDto> listDish = new ArrayList<DishInOrderDishDto>();							// xu ly check kho
						DishInOrderDishDto dish = null;
						for (OrderDish orderDish : listOrderDish) {													// l???y c??c dish id v?? quantity
							if(orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_ORDERED) {	// ch??? l???y l???i nvl nh???ng orderdish ??ang ordered	
								dish = new DishInOrderDishDto();
								dish.setOrderDishId(orderDish.getOrderDishId());
								dish.setDishId(orderDish.getDish().getDishId());
								dish.setQuantity(orderDish.getQuantityOk());
								listDish.add(dish);																	// l??u l???i dish ????
							}
						}
						
						List<GetQuantifierMaterialDto> listQuantifier = null;
						Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> mapDish = new HashMap<DishInOrderDishDto, List<GetQuantifierMaterialDto>>();
						for (DishInOrderDishDto dishIn : listDish) {													//m???i dish s??? t????ng ???ng v???i 1 list c??c quantifiers
							listQuantifier = new ArrayList<GetQuantifierMaterialDto>();
							listQuantifier = orderRepo.findListQuantifierMaterialByDish(dishIn.getDishId());
							if(listQuantifier.size()!= 0) {										
								mapDish.put(dishIn, listQuantifier);												// t??m ra material ?????i v???i dish l?? ordered				
							}
						}
						map = CheckMaterialUtils.testKho(mapDish);														// x??? l?? ra th??nh c??c nguy??n v???t li???u
						
						//export here: l???y l???i nguy??n v???t li???u export tr?????c ????
						Long exportId = exportRepo.findByOrderId(dto.getOrderId());
						if(exportId != null) {
							Export export = exportRepo.findById(exportId).orElseThrow(
									() -> new NotFoundException("Not found ExportId: " + exportId));
							
							List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();		
							Material material = null;
							Double remainBack = 0d, totalExportBack = 0d, quantityExportBack = 0d;														// upadate l???i material, exportmaterial
							
							for (Long materialId : map.keySet()) {
								for (ExportMaterial exportMaterial : export.getExportMaterials()) {
									if(materialId == exportMaterial.getMaterial().getMaterialId() ) {
										material = exportMaterial.getMaterial();											// l???y ra material ????	
										
										remainBack = Utils.sumBigDecimalToDouble(material.getRemain(), map.get(materialId));						// remain c??n l???i, c???ng th??m s??? l?????ng c?? th??? c???ng
										totalExportBack = Utils.subtractBigDecimalToDouble(material.getTotalExport(), map.get(materialId));			// gi???m l??n s??? l?????ng export
										quantityExportBack = Utils.subtractBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update l???i s??? l?????ng export
										
										material.setTotalExport(totalExportBack);
										material.setRemain(remainBack);
										exportMaterial.setMaterial(material);
										exportMaterial.setQuantityExport(quantityExportBack);
										exportMaterials.add(exportMaterial);												// l??u l???i v??o list
									}							
								}
							}				
//							export.setExportMaterials(exportMaterials);													// l??u l???i v??o export
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
							exportRepo.save(export);																		// l??u v??o database
							// export end
						}
						
						for (OrderDish orderDish : listOrderDish) {
							if(orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_ORDERED) {
								orderDishRepo.updateQuantity(0, 0, orderDish.getSellPrice(), 0d, orderDish.getOrderDishId());
							}
							orderDishOptionRepo.updateCancel(StatusConstant.STATUS_ORDER_DISH_OPTION_CANCELED, orderDish.getOrderDishId());
						}
						orderDishRepo.updateCancelByOrder(StatusConstant.STATUS_ORDER_DISH_CANCELED, dto.getComment(), 
								Utils.getCurrentTime(), dto.getModifiedBy(), dto.getOrderId());
					}
				
					tableRepo.updateToReady(dto.getTableId(), StatusConstant.STATUS_TABLE_READY);
					
					result = orderRepo.updateCancelOrder(StatusConstant.STATUS_ORDER_CANCELED, Utils.getCurrentTime(), dto.getModifiedBy(), dto.getComment(), dto.getOrderId());
				}else {																// ???? s??? d???ng nguy??n v???t li???u, ch??? canceled, ko back l???i nvl
					List<Long> listOrderDishId = orderDishRepo.findOrderDishId(dto.getOrderId());
					if(listOrderDishId.size() != 0) {
						for (Long orderDishId : listOrderDishId) {
							orderDishOptionRepo.updateCancel(StatusConstant.STATUS_ORDER_DISH_OPTION_CANCELED, orderDishId);
						}
					}		
					orderDishRepo.updateCancelByOrder(StatusConstant.STATUS_ORDER_DISH_CANCELED, dto.getComment(), Utils.getCurrentTime(), dto.getModifiedBy(), dto.getOrderId());
					tableRepo.updateToReady(dto.getTableId(), StatusConstant.STATUS_TABLE_READY);
					result = orderRepo.updateCancelOrder(StatusConstant.STATUS_ORDER_CANCELED, Utils.getCurrentTime(), dto.getModifiedBy(), dto.getComment(), dto.getOrderId());
				}
				simpMessagingTemplate.convertAndSend("/topic/chef", getListDisplayChefScreen());
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
				
			} catch (NullPointerException e) {
				throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
			}
		}
		
		return result;
	}


	/**
	 * b???p nh???n x??c nh???n ???? nh??n c??? order: PREPARATION, b???t d???u n???u. N???u status l?? COMPLETED th?? l?? ???? n???u xong
	 */
	@Override
	@Transactional
	public OrderChefDto updateOrderChef(OrderRequest request) {	// hi???n th??? theo b??n, b???p ch???n c??? b??n
		OrderChefDto orderChef = null;
		try {
			int result = 0;
			if(request != null && request.getOrderId() != null && request.getChefStaffId() != null) {
				if(request.getStatusId() == StatusConstant.STATUS_ORDER_PREPARATION) {							// thay ?????i tr???ng th??i c???a c??c th???ng orderdish
					result = orderDishRepo.updateStatusByOrder(StatusConstant.STATUS_ORDER_DISH_PREPARATION, request.getOrderId());
				}else if(request.getStatusId() == StatusConstant.STATUS_ORDER_COMPLETED) {						// thay ?????i tr???ng th??i c???a c??c th???ng orderdish
					result = orderDishRepo.updateStatusByOrder(StatusConstant.STATUS_ORDER_DISH_COMPLETED, request.getOrderId());
				}
				if(result != 0) {
					result = orderRepo.updateOrderChef(request.getChefStaffId(), request.getStatusId(), request.getOrderId());
					orderChef = getOrderChefById(request.getOrderId());
				}
				simpMessagingTemplate.convertAndSend("/topic/chef", getListDisplayChefScreen());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+request.getOrderId(), getOrderDetailById(request.getOrderId()));		// socket
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
			}
			
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return orderChef;
	}
	
	/**
	 * order th???c hi???n g???i y??u c???u thanh to??n
	 */
	@Override
	@Transactional
	public String updateWaitingPayOrder(OrderRequest dto) {
		
		Long statusOrder = null;
		try {
			if(dto != null && dto.getOrderId() != null) {
				statusOrder = orderRepo.findStatusOrderById(dto.getOrderId());
				
				if(statusOrder.equals(StatusConstant.STATUS_ORDER_COMPLETED)) {
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT, dto.getOrderId());		//g???i y??u c???u
				}else if(statusOrder.equals(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT)){
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_COMPLETED, dto.getOrderId());				// r??t l???i g???i y??u c???u
				}else {
					return "B??n n??y ch??a y??u c???u thanh to??n ???????c";
				}
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
			}
			return "";
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
	}
	
	/**
	 * thu ng??n ch???p nh???n thanh to??n
	 */
	@Override
	@Transactional
	public String updateAcceptPaymentOrder(OrderRequest dto, Integer accept) {
		String result = "";
		Long statusOrder = null;
		try {
			if(dto != null && dto.getOrderId() != null) {
				statusOrder = orderRepo.findStatusOrderById(dto.getOrderId());
				if(statusOrder.equals(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT) && accept==1) {									// ch???p nh???n
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_ACCEPTED_PAYMENT, dto.getOrderId());
					simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
					simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
				}else if (statusOrder.equals(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT) && accept==0){							// ko ch???p nh???n
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_COMPLETED, dto.getOrderId());
					simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
					simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
				}else {
					return "B??n n??y ch??a ch???p nh???n thanh to??n ???????c";
				}
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return result;
	}

	/**
	 * thanh to??n
	 */
	@Override
	@Transactional
	public int updatePaymentOrder(OrderRequest dto) {
		int result = 0;
		OrderDetailDto orderDetail = null;
		if(dto != null) {
			try {
				if(dto.getOrderId() != null) {
					orderDetail = new OrderDetailDto();
					orderDetail = getOrderDetailById(dto.getOrderId());
					if(orderDetail.getStatusId() == StatusConstant.STATUS_ORDER_COMPLETED || orderDetail.getStatusId() == StatusConstant.STATUS_ORDER_ACCEPTED_PAYMENT) {
						String timeToComplete = Utils.getOrderTime(Utils.getCurrentTime(), orderDetail.getOrderDate());
						result = orderRepo.updatePaymentOrder(Utils.getCurrentTime(), dto.getCustomerPayment(), dto.getCashierStaffId(), 
								StatusConstant.STATUS_ORDER_DONE, timeToComplete, dto.getOrderId());
						if(result != 0) {
							if(orderDetail.getOrderDish().size() != 0) {									// order n??y c?? c??c m??n ??n
								ReportDishTrendDto reportDto = null;
								for (OrderDishDto orderDish : orderDetail.getOrderDish()) {
									if(orderDish.getQuantity() != 0) {										// c?? g???i m??n
										reportDto = new ReportDishTrendDto();								// add v??o list trend ????? show list trend dish
										reportDto.setDishId(orderDish.getDish().getDishId());
										reportDto.setDishName(orderDish.getDish().getDishName());
										reportDto.setDishUnit(orderDish.getDish().getDishUnit());
										reportDto.setMaterialCost(orderDish.getDish().getCost());
										reportDto.setDishCost(orderDish.getDish().getDishCost());
										reportDto.setUnitPrice(orderDish.getSellPrice());
										reportDto.setQuantityOk(orderDish.getQuantityOk());
										reportDto.setQuantityCancel(orderDish.getQuantityCancel());
										reportDto.setOrderCode(orderDetail.getOrderCode());
										reportDto.setCategoryId(1L);
										reportDto.setStatusId(orderDish.getStatusStatusId());
										reportDto.setOrderDishId(orderDish.getOrderDishId());
										reportService.insertReportDishTrend(reportDto);
									}
								}
							}
							
							Tables entity = tableRepo.findById(orderDetail.getTableId()).get();
							entity.setOrder(null);
							Status status = statusRepo.findById(StatusConstant.STATUS_TABLE_READY).get();
							entity.setStatus(status);														// set l???i status
							tableRepo.save(entity);
						}
						
						simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
					}
				}
			} catch (Exception e) {
				throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
			}
			
		}
		return result;
	}

	/**
	 * update v??? s??? l?????ng
	 */
	@Override
	@Transactional
	public int updateOrderQuantity(Integer totalItem, Double totalAmount, Long orderId) {
		int result = 0;
		try {
			if(orderId != null) {
				result = orderRepo.updateOrderQuantity(totalItem, totalAmount, orderId);
			}
		} catch (NullPointerException e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return result;
	}

	/**
	 * select detail by id
	 */
	@Override
	public OrderDetailDto getOrderDetailById(Long orderId) {
		Order entity= null;
		OrderDetailDto detail = null;
		try {
			if(orderId != null) {
				entity = orderRepo.findOrderById(orderId);
				detail = orderMapper.entityToDetail(entity);
			}
		} catch (NullPointerException e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
		return detail;
	}

	/**
	 * update comment cho order
	 */
	@Override
	@Transactional
	public int updateComment(OrderRequest request) {
		int result = 0;
		try {
			if(request != null && request.getOrderId() != null) {
				result = orderRepo.updateComment(request.getComment(), request.getOrderId());
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/chef", getListDisplayChefScreen());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+request.getOrderId(), getOrderDetailById(request.getOrderId()));		// socket
			}
			
		} catch (NullPointerException e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return result;
	}

	/*
	 * hi???n th??? m??n h??nh b???p
	 */
	@Override
	public List<OrderChefDto> getListDisplayChefScreen() {
		
		try {
			List<Order> listEntity = orderRepo.findListOrderChef();
			
			List<OrderChefDto> listOrderChef = new ArrayList<OrderChefDto>();
			if(listEntity.size() != 0) {
				listOrderChef = listEntity.stream().map(orderMapper::entityToChef).collect(Collectors.toList());
			}

			listOrderChef = listOrderChef.stream()															
								.filter(orderChef -> (!orderChef.getTotalQuantity().equals(0)))			// n???u c??i n??o quantity = 0 th?? ko hi???n
								.sorted(Comparator.comparing(OrderChefDto::getCreatedDate))				// s???p x???p 
								.collect(Collectors.toList());
			return listOrderChef;
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		
	}
	

	@Override
	public OrderChefDto getOrderChefById(Long orderId) {
		OrderChefDto orderChef = null;
		try {
			if(orderId != null) {
				Order entity = orderRepo.findOrderById(orderId);
				orderChef = orderMapper.entityToChef(entity);
			}
		} catch (Exception e) {
			throw new NullPointerException("C?? g?? ???? kh??ng ????ng x???y ra");
		}
		return orderChef;
	}


}
