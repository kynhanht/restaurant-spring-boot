package fu.rms.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "report_dish_trend")
@Getter
@Setter
public class ReportDishTrend {

	@Id
	@Column(name="report_dish_trend_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long reportDishTrendId;
	
	@Column(name="dish_id")
	private Long dishId;
	
	@Column(name="dish_name")
	private String dishName;
	
	@Column(name="dish_unit")
	private String dishUnit;				// đơn vị thời điểm đó
	
	@Column(name="material_cost")
	private Double materialCost;			// chi phí nguyên vật liệu
	
	@Column(name="dish_cost")
	private Double dishCost;				// chi phí giá thành sản phẩm
	
	@Column(name="unit_price")
	private Double unitPrice;				// giá trên 1 đơn vị
	
	@Column(name="quantity_ok")
	private Integer quantityOk;				// số lượng bán ra
	
	@Column(name="quantity_cancel")
	private Integer quantityCancel;			// số lượng hủy
	
	@Column(name="order_code")
	private String orderCode;				// orderCode của order đó
	
	@Column(name="category_id")
	private Long categoryId;					// thể loại của dish đó
	
	@Column(name="status_id")
	private Long statusId;					// trạng thái của món ăn đó
	
	@Column(name="order_dish_id")
	private Long orderDishId;				//orderdish : để lấy option ra
	
	@Column(name="created_date")
	private Timestamp createdDate;			// ngày thanh toán món đó
	
}
