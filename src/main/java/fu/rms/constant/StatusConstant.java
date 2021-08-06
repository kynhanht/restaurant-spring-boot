package fu.rms.constant;

public class StatusConstant {

	/*
	 * Status Location table
	 */
	public static final Long STATUS_LOCATION_TABLE_READY = (long) 1;
	public static final Long STATUS_LOCATION_TABLE_EXPIRE = (long) 2;
	public static final Long STATUS_LOCATION_TABLE_FULL = (long) 3;
	
	/*
	 * Status table
	 */
	public static final Long STATUS_TABLE_READY = (long) 4;
	public static final Long STATUS_TABLE_BUSY = (long) 5;
	public static final Long STATUS_TABLE_ORDERED = (long) 6;
	
	/*
	 * Status dish
	 */
	public static final Long STATUS_DISH_AVAILABLE=(long) 7;
	public static final Long STATUS_DISH_EXPIRE=(long) 8;
	public static final Long STATUS_DISH_OVER=(long) 9;

	
	/*
	 * Order status
	 */
	public static final Long STATUS_ORDER_ORDERING = (long) 10;
	public static final Long STATUS_ORDER_ORDERED = (long) 11;
	public static final Long STATUS_ORDER_PREPARATION = (long) 12;
//	public static final Long STATUS_ORDER_JUST_COOKED = (long) 13;
	public static final Long STATUS_ORDER_COMPLETED = (long) 13;
	public static final Long STATUS_ORDER_WAITING_FOR_PAYMENT = (long) 14;
	public static final Long STATUS_ORDER_ACCEPTED_PAYMENT = (long) 15;
	public static final Long STATUS_ORDER_DONE = (long) 16;
	public static final Long STATUS_ORDER_CANCELED = (long) 17;
	
	/*
	 * Order dish status
	 */
	public static final Long STATUS_ORDER_DISH_ORDERED=(long) 18;
	public static final Long STATUS_ORDER_DISH_PREPARATION=(long) 19;
//	public static final Long STATUS_ORDER_DISH_JUST_COOKED=(long) 20;
	public static final Long STATUS_ORDER_DISH_COMPLETED=(long) 20;
	public static final Long STATUS_ORDER_DISH_OK_CANCEL=(long) 21;		// hủy 1 phần món ăn
	public static final Long STATUS_ORDER_DISH_CANCELED=(long) 22;
	
	/*
	 * Option dish status
	 */
	public static final Long STATUS_OPTION_AVAILABLE=(long) 23;
	public static final Long STATUS_OPTION_EXPIRE=(long) 24;
	
	/*
	 * Order Dish Option status
	 */
	public static final Long STATUS_ORDER_DISH_OPTION_DONE=(long) 25;
	public static final Long STATUS_ORDER_DISH_OPTION_CANCELED=(long) 26;
	
	/*
	 * Category Status
	 */
	public static final Long STATUS_CATEGORY_AVAILABLE=(long) 27;
	public static final Long STATUS_CATEGORY_EXPIRE=(long) 28;
	
	/*
	 * Material Status
	 */
	public static final Long STATUS_MATERIAL_AVAILABLE=(long) 29;
	public static final Long STATUS_MATERIAL_EXPIRE=(long) 30;
	public static final Long STATUS_MATERIAL_NOTIFICATION=(long) 31;	// xuống đến mức thông báo
	
	/*
	 * Kiểm kê Status
	 */
	public static final Integer INVENTORY_NONE = 0;					// ko làm gì
	public static final Integer INVENTORY_ADD = 1;					// xử lý nhập thêm
	public static final Integer INVENTORY_MINUS = 2;				// xử lý xuất đi
	
}
