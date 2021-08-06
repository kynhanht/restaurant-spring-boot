package fu.rms.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_dish")
@Getter
@Setter
public class OrderDish {

	@Id
	@Column(name = "order_dish_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long orderDishId;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="modified_by")
	private String modifiedBy;
	
	@Column(name="modified_date")
	private Timestamp modifiedDate;
	
	@Column(name="create_by")
	private String createBy;
	
	@Column(name="create_date")
	private Timestamp createDate;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="comment_cancel")
	private String commentCancel;
	
	@Column(name="quantity_cancel")		// hủy số lượng món trong order dish
	private Integer quantityCancel;
	
	@Column(name="quantity_ok")			// = quantity - quantityCancel
	private Integer quantityOk;
	
	@Column(name="sell_price")
	private Double sellPrice;
	
	@Column(name="sum_price")
	private Double sumPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dish_id")
	private Dish dish;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	@OneToMany(mappedBy = "orderDish")
	private List<OrderDishOption> orderDishOptions;
	
	@OneToMany(mappedBy = "orderDish")
	private List<OrderDishCancel> orderDishCancels;
}
