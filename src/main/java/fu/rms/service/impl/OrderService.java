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
	 * tạo mới order
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
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
					for (OrderDishDto orderDishDto : dto.getOrderDish()) {										// lấy các dish id và quantity
						dish = new DishInOrderDishDto();
						dish.setOrderDishId(orderDishDto.getOrderDishId());
						dish.setDishId(orderDishDto.getDish().getDishId());
						dish.setQuantity(orderDishDto.getQuantity());
						listDish.add(dish);
					}
					
					List<GetQuantifierMaterialDto> listQuantifier = null;
					List<GetQuantifierMaterialDto> listQuantifiers = new ArrayList<GetQuantifierMaterialDto>();
					Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> mapDish = new HashMap<DishInOrderDishDto, List<GetQuantifierMaterialDto>>();
					for (DishInOrderDishDto dishIn : listDish) {													//mỗi dish sẽ tương ứng với 1 list các quantifiers
						listQuantifier = new ArrayList<GetQuantifierMaterialDto>();
						listQuantifier = orderRepo.findListQuantifierMaterialByDish(dishIn.getDishId());
						if(listQuantifier.size()!= 0) {
							listQuantifiers.addAll(listQuantifier);													// add vao list tong
							mapDish.put(dishIn, listQuantifier);
						}
					}
					Set<String> setMaterialName = new HashSet<String>();
					if(!mapDish.isEmpty()) {																		// map phải có phần tử
						checkEmptyMaterial = false;
						map = CheckMaterialUtils.testKho(mapDish);														// xử lý ra thành các nguyên vật liệu
						Set<Long> listDishId = new LinkedHashSet<Long>();
						for (Long materialId : map.keySet()) {
							RemainDto remain = materialRepo.findRemainById(materialId);
							Double remainMaterial = remain.getRemain();
							if(map.get(materialId) > remainMaterial) {												// neu nvl can > nvl con lai
								for (GetQuantifierMaterialDto getQuantifierMaterial : listQuantifiers) {
									if(materialId == getQuantifierMaterial.getMaterialId()) {						//tim kiem cac dish co material thieu
										listDishId.add(getQuantifierMaterial.getDishId());							//luu lai dish id trung su dung set ko luu cac id trung
//										map2.put(materialId, map.get(materialId));									// lưu lại material thiếu
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
							for (Long dishId : listDishId) {														//các món ăn ko đủ nvl
								boolean checkMax = false;
								listQuantifierCheck = new ArrayList<GetQuantifierMaterialDto>();
								listQuantifierCheck = orderRepo.findListQuantifierMaterialByDish(dishId);
								mapDish2 = new HashMap<Long, List<GetQuantifierMaterialDto>>();
								map2 = new HashMap<Long, Double>();
								mapDish2.put(dishId, listQuantifierCheck);
								map2 = CheckMaterialUtils.calculateMaterial(mapDish2);
								if(listQuantifierCheck.size()!= 0) {
									max=0;
									for (Long materialId : map.keySet()) {											// map chứa các material tất cả
										RemainDto remain = materialRepo.findRemainById(materialId);
										Double remainMaterial = remain.getRemain();
//										if(map.get(materialId) > remainMaterial) {
											for (Long materialIdDish : map2.keySet()) {									// map chứa dish của 1 thằng
												if(materialIdDish.equals(materialId)) {
													if(max == 0 && !checkMax) {																	// lần đầu tìm đc nvl
														double quantity = remainMaterial/map2.get(materialIdDish);
														max = (int) quantity;
													}else {
														double quantity = remainMaterial/map2.get(materialIdDish);
														if((int) quantity < max) {													// tìm dc thằng khác nhỏ hơn
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
//										if(mapNumber.get(dishId) < orderDish.getQuantity()) {						// chỉ hiển thị thằng nào số nvl ko đủ cho số lượng đó
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
					if(statusOrder == StatusConstant.STATUS_ORDER_ORDERING) {										// chưa order thì update trạng thái, ngày order
						Date orderDate = Utils.getCurrentTime();
						orderRepo.updateSaveOrder(StatusConstant.STATUS_ORDER_ORDERED, orderDate, dto.getTotalItem(), 
								dto.getTotalAmount(), dto.getComment(), dto.getOrderId());
					} else { 																							// nếu đã order rồi thì chỉ update số lượng và giá
						orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_ORDERED, dto.getOrderId());
						updateOrderQuantity(dto.getTotalItem(), dto.getTotalAmount(), dto.getOrderId());
					}
					tableService.updateStatusOrdered(dto.getTableId(), StatusConstant.STATUS_TABLE_ORDERED);
				}
			} catch (NullPointerException e) {
				throw new NullPointerException("Có gì đó không đúng xảy ra");
			} catch (Exception e) {
				throw new NullPointerException("Có gì đó không đúng xảy ra");
			}
		
			try {
				if(!checkEmptyMaterial) {														// có nguyên liệu
					//export here																// tạo mới export
					if(statusOrder == StatusConstant.STATUS_ORDER_ORDERING) {					// lần đầu order
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
							exportMaterial.setQuantityExport(Utils.bigDecimalToDouble(map.get(materialId)));											// lấy tổng số lượng material
							exportMaterial.setUnitPrice(material.getUnitPrice());
							
							remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));		// remain còn lại: trừ đi số lượng export
							totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));	// tăng lên số lượng export			
							
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
						// sửa lại export trước đó
						try {
							
							Export export = null;																			// tăng số lượng
							Long exportId = exportRepo.findByOrderId(dto.getOrderId());										// lấy ra export id theo order id
							if(exportId != null) {
								export = exportRepo.findById(exportId).orElseThrow(
										() -> new NotFoundException("Not found Export: " + exportId));
								
								List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();		
								Material material = null;
								Double remainNew = 0d, totalExportNew = 0d, quantityExportNew = 0d;
								boolean checkMaterial=false;
								ExportMaterial exportMaterialNew;
								for (Long materialId : map.keySet()) {															// upadate lại material, exportmaterial
									checkMaterial=false;
									for (ExportMaterial exportMaterial : export.getExportMaterials()) {
										if(materialId == exportMaterial.getMaterial().getMaterialId()) {						// tìm material liên quan đến món ăn đó
											material = exportMaterial.getMaterial();											// lấy ra material đó
											
											remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));			// remain còn lại: trừ đi số lượng export
											totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));		// tăng lên số lượng export
											quantityExportNew = Utils.sumBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update lại số lượng export
											
											material.setTotalExport(totalExportNew);
											material.setRemain(remainNew);
											exportMaterial.setMaterial(material);
											exportMaterial.setQuantityExport(quantityExportNew);
											exportMaterials.add(exportMaterial);												// lưu lại vào list
											checkMaterial=true;																	//nvl này đã có trong lần export trước đó
										}
									}
									if(!checkMaterial) {																		// nvl này chưa có trong lần export trước đó, thì sẽ add thêm																		
										remainNew = 0d;
										totalExportNew = 0d;
										exportMaterialNew = new ExportMaterial();
										material = materialRepo.findById(materialId).orElseThrow(								// select lại material đó
												() -> new NotFoundException("Not found MaterialId: " + materialId));
										exportMaterialNew.setQuantityExport(Utils.bigDecimalToDouble(map.get(materialId)));		// lấy tổng số lượng material
										exportMaterialNew.setUnitPrice(material.getUnitPrice());
										
										remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));		// remain còn lại: trừ đi số lượng export
										totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));	// tăng lên số lượng export
										
										material.setTotalExport(totalExportNew);
										material.setRemain(remainNew);
										
										exportMaterialNew.setMaterial(material);
										exportMaterialNew.setExport(export);
										exportMaterials.add(exportMaterialNew);
										
									}
								}
								Iterator<ExportMaterial> exportIte = export.getExportMaterials().iterator();					// trừ đi thằng nào đã có material trong export trước đó
								while (exportIte.hasNext()) {
									Long materialId = exportIte.next().getMaterial().getMaterialId();
									for (ExportMaterial exportMaterial : exportMaterials) {
										if(materialId == exportMaterial.getMaterial().getMaterialId()) {
											exportIte.remove();																	// tìm được thằng nào đã có trước đó thì xóa
											break;
										}
									}
								}
								export.getExportMaterials().addAll(exportMaterials);											// add thêm thằng nào chưa có material trong export trước đó
								exportRepo.save(export);																		// lưu vào database
								// end sửa export
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
									exportMaterial.setQuantityExport(Utils.bigDecimalToDouble(map.get(materialId)));											// lấy tổng số lượng material
									exportMaterial.setUnitPrice(material.getUnitPrice());
									
									remainNew = Utils.subtractBigDecimalToDouble(material.getRemain(), map.get(materialId));		// remain còn lại: trừ đi số lượng export
									totalExportNew = Utils.sumBigDecimalToDouble(material.getTotalExport(), map.get(materialId));	// tăng lên số lượng export

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
							throw new NullPointerException("Có gì đó không đúng xảy ra");
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
	 * thay đổi bàn
	 */
	@Override
	@Transactional
	public String changeOrderTable(OrderDto dto, Long tableId) {
		
		String result = "";
		int update = 0;
		try {
			if(dto != null && dto.getOrderId() != null && tableId != null) {
				Long statusTable = tableRepo.findStatusByTableId(tableId);
				if(statusTable == StatusConstant.STATUS_TABLE_ORDERED) {										// bàn đang bận thì ko đổi được
					return AppMessageErrorConstant.TABLE_ORDERED;
				}else if(statusTable == StatusConstant.STATUS_TABLE_BUSY) {										// bàn đang bận thì ko đổi được
					return AppMessageErrorConstant.TABLE_BUSY;
				}else {
					if(dto.getTableId() != null) {
						tableRepo.updateToReady(dto.getTableId(), StatusConstant.STATUS_TABLE_READY); 			// đổi bàn cũ thành trạng thái ready
						if(dto.getStatusId() == StatusConstant.STATUS_ORDER_ORDERING) {
							tableService.updateTableNewOrder(dto.getOrderId(), tableId, StatusConstant.STATUS_TABLE_BUSY);				// đổi bàn mới thành trạng thái theo order đổi
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
		}
		return result;
	}

	/**
	 * hủy order
	 */
	@Override
	@Transactional
	public int updateCancelOrder(OrderDto dto) {
		int result = 0;
		if(dto != null && dto.getOrderId() != null && dto.getStatusId() != null) {
			try {
				if(dto.getStatusId() == StatusConstant.STATUS_ORDER_ORDERING) { 									// mới tạo order, chưa chọn món
					try {
						tableRepo.updateToReady(dto.getTableId(), StatusConstant.STATUS_TABLE_READY);
						result = orderRepo.updateCancelOrder(StatusConstant.STATUS_ORDER_CANCELED, Utils.getCurrentTime(), dto.getModifiedBy(), dto.getComment(), dto.getOrderId());
					} catch (Exception e) {
						return AppMessageErrorConstant.RETURN_ERROR_NULL;
					}	
				}else if(dto.getStatusId() == StatusConstant.STATUS_ORDER_ORDERED) {								// mới order thì có thể hủy order, back lại nvl
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
						for (OrderDish orderDish : listOrderDish) {													// lấy các dish id và quantity
							if(orderDish.getStatus().getStatusId() == StatusConstant.STATUS_ORDER_DISH_ORDERED) {	// chỉ lấy lại nvl những orderdish đang ordered	
								dish = new DishInOrderDishDto();
								dish.setOrderDishId(orderDish.getOrderDishId());
								dish.setDishId(orderDish.getDish().getDishId());
								dish.setQuantity(orderDish.getQuantityOk());
								listDish.add(dish);																	// lưu lại dish đó
							}
						}
						
						List<GetQuantifierMaterialDto> listQuantifier = null;
						Map<DishInOrderDishDto, List<GetQuantifierMaterialDto>> mapDish = new HashMap<DishInOrderDishDto, List<GetQuantifierMaterialDto>>();
						for (DishInOrderDishDto dishIn : listDish) {													//mỗi dish sẽ tương ứng với 1 list các quantifiers
							listQuantifier = new ArrayList<GetQuantifierMaterialDto>();
							listQuantifier = orderRepo.findListQuantifierMaterialByDish(dishIn.getDishId());
							if(listQuantifier.size()!= 0) {										
								mapDish.put(dishIn, listQuantifier);												// tìm ra material đối với dish là ordered				
							}
						}
						map = CheckMaterialUtils.testKho(mapDish);														// xử lý ra thành các nguyên vật liệu
						
						//export here: lấy lại nguyên vật liệu export trước đó
						Long exportId = exportRepo.findByOrderId(dto.getOrderId());
						if(exportId != null) {
							Export export = exportRepo.findById(exportId).orElseThrow(
									() -> new NotFoundException("Not found ExportId: " + exportId));
							
							List<ExportMaterial> exportMaterials = new ArrayList<ExportMaterial>();		
							Material material = null;
							Double remainBack = 0d, totalExportBack = 0d, quantityExportBack = 0d;														// upadate lại material, exportmaterial
							
							for (Long materialId : map.keySet()) {
								for (ExportMaterial exportMaterial : export.getExportMaterials()) {
									if(materialId == exportMaterial.getMaterial().getMaterialId() ) {
										material = exportMaterial.getMaterial();											// lấy ra material đó	
										
										remainBack = Utils.sumBigDecimalToDouble(material.getRemain(), map.get(materialId));						// remain còn lại, cộng thêm số lượng có thể cộng
										totalExportBack = Utils.subtractBigDecimalToDouble(material.getTotalExport(), map.get(materialId));			// giảm lên số lượng export
										quantityExportBack = Utils.subtractBigDecimalToDouble(exportMaterial.getQuantityExport(), map.get(materialId));	// update lại số lượng export
										
										material.setTotalExport(totalExportBack);
										material.setRemain(remainBack);
										exportMaterial.setMaterial(material);
										exportMaterial.setQuantityExport(quantityExportBack);
										exportMaterials.add(exportMaterial);												// lưu lại vào list
									}							
								}
							}				
//							export.setExportMaterials(exportMaterials);													// lưu lại vào export
							Iterator<ExportMaterial> exportIte = export.getExportMaterials().iterator();					// trừ đi thằng nào đã có material trong export trước đó
							while (exportIte.hasNext()) {
								Long materialId = exportIte.next().getMaterial().getMaterialId();
								for (ExportMaterial exportMaterial : exportMaterials) {
									if(materialId == exportMaterial.getMaterial().getMaterialId()) {
										exportIte.remove();																	// tìm được thằng nào đã có trước đó thì xóa
										break;
									}
								}
							}
							export.getExportMaterials().addAll(exportMaterials);
							exportRepo.save(export);																		// lưu vào database
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
				}else {																// đã sử dụng nguyên vật liệu, chỉ canceled, ko back lại nvl
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
				throw new NullPointerException("Có gì đó không đúng xảy ra");
			}
		}
		
		return result;
	}


	/**
	 * bếp nhấn xác nhận đã nhân cả order: PREPARATION, bắt dầu nấu. Nếu status là COMPLETED thì là đã nấu xong
	 */
	@Override
	@Transactional
	public OrderChefDto updateOrderChef(OrderRequest request) {	// hiển thị theo bàn, bếp chọn cả bàn
		OrderChefDto orderChef = null;
		try {
			int result = 0;
			if(request != null && request.getOrderId() != null && request.getChefStaffId() != null) {
				if(request.getStatusId() == StatusConstant.STATUS_ORDER_PREPARATION) {							// thay đổi trạng thái của các thằng orderdish
					result = orderDishRepo.updateStatusByOrder(StatusConstant.STATUS_ORDER_DISH_PREPARATION, request.getOrderId());
				}else if(request.getStatusId() == StatusConstant.STATUS_ORDER_COMPLETED) {						// thay đổi trạng thái của các thằng orderdish
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
		}
		return orderChef;
	}
	
	/**
	 * order thực hiện gửi yêu cầu thanh toán
	 */
	@Override
	@Transactional
	public String updateWaitingPayOrder(OrderRequest dto) {
		
		Long statusOrder = null;
		try {
			if(dto != null && dto.getOrderId() != null) {
				statusOrder = orderRepo.findStatusOrderById(dto.getOrderId());
				
				if(statusOrder.equals(StatusConstant.STATUS_ORDER_COMPLETED)) {
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT, dto.getOrderId());		//gửi yêu cầu
				}else if(statusOrder.equals(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT)){
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_COMPLETED, dto.getOrderId());				// rút lại gửi yêu cầu
				}else {
					return "Bàn này chưa yêu cầu thanh toán được";
				}
				simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
				simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
			}
			return "";
		} catch (Exception e) {
			throw new NullPointerException("Có gì đó không đúng xảy ra");
		}
	}
	
	/**
	 * thu ngân chấp nhận thanh toán
	 */
	@Override
	@Transactional
	public String updateAcceptPaymentOrder(OrderRequest dto, Integer accept) {
		String result = "";
		Long statusOrder = null;
		try {
			if(dto != null && dto.getOrderId() != null) {
				statusOrder = orderRepo.findStatusOrderById(dto.getOrderId());
				if(statusOrder.equals(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT) && accept==1) {									// chấp nhận
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_ACCEPTED_PAYMENT, dto.getOrderId());
					simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
					simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
				}else if (statusOrder.equals(StatusConstant.STATUS_ORDER_WAITING_FOR_PAYMENT) && accept==0){							// ko chấp nhận
					orderRepo.updateStatusOrder(StatusConstant.STATUS_ORDER_COMPLETED, dto.getOrderId());
					simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
					simpMessagingTemplate.convertAndSend("/topic/orderdetail/"+dto.getOrderId(), getOrderDetailById(dto.getOrderId()));		// socket
				}else {
					return "Bàn này chưa chấp nhận thanh toán được";
				}
			}
		} catch (Exception e) {
			throw new NullPointerException("Có gì đó không đúng xảy ra");
		}
		return result;
	}

	/**
	 * thanh toán
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
							if(orderDetail.getOrderDish().size() != 0) {									// order này có các món ăn
								ReportDishTrendDto reportDto = null;
								for (OrderDishDto orderDish : orderDetail.getOrderDish()) {
									if(orderDish.getQuantity() != 0) {										// có gọi món
										reportDto = new ReportDishTrendDto();								// add vào list trend để show list trend dish
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
							entity.setStatus(status);														// set lại status
							tableRepo.save(entity);
						}
						
						simpMessagingTemplate.convertAndSend("/topic/tables", tableService.getListTable());
					}
				}
			} catch (Exception e) {
				throw new NullPointerException("Có gì đó không đúng xảy ra");
			}
			
		}
		return result;
	}

	/**
	 * update về số lượng
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
		}
		return result;
	}

	/*
	 * hiển thị màn hình bếp
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
								.filter(orderChef -> (!orderChef.getTotalQuantity().equals(0)))			// nếu cái nào quantity = 0 thì ko hiện
								.sorted(Comparator.comparing(OrderChefDto::getCreatedDate))				// sắp xếp 
								.collect(Collectors.toList());
			return listOrderChef;
		} catch (Exception e) {
			throw new NullPointerException("Có gì đó không đúng xảy ra");
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
			throw new NullPointerException("Có gì đó không đúng xảy ra");
		}
		return orderChef;
	}


}
