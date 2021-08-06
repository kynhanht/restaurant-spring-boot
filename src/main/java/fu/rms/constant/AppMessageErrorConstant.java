package fu.rms.constant;

import org.slf4j.Logger;

public class AppMessageErrorConstant {


	public static final String ALPHA_NUMBERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public static final int RETURN_ERROR_NULL = 0;
	public static final String CANCEL_NOT_MORE_THAN_OK = "Số lượng hủy nhiều quá";
	public static final String NO_DATA = "Không có data";
	public static final String STATUS_NOT_CHANGE = "Trạng thái này không được đổi";
	public static final String CHANGE_SUCCESS = "Thay đổi thành công";
	public static final String CHANGE_ERROR= "Thay đổi thất bại";
	public static final String INSERT_SUCCESS = "Thêm mới thành công";
	public static final String INSERT_ERROR = "Thêm mới thất bại";
	public static final String NULL = "Null rồi";
	public static final String NO_CHANGE_DATA = "Không có gì thay đổi";
	
	public static final String INPUT_WRONG = "Nhập không đúng";
	
	public static Logger logger = null;
	
	public static final String TABLE_BUSY = "Bàn này đang bận";
	public static final String TABLE_ORDERED = "Bàn này đang có khách ngồi";
	public static final String TABLE_READY = "Có thể đổi";
	public static final String TABLE_ERROR = "Đổi bàn không thành công";
	
	public static final Double MATERIAL_EXPORT_ZERO = (double) 0;
	
	public static final String QUANTITY_RETURN = "Số lượng trả lớn hơn số lượng gọi";
	
	
}
