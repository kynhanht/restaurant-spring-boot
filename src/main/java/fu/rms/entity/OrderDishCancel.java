package fu.rms.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_dish_cancel")
@Getter
@Setter
public class OrderDishCancel {

	@Id
	@Column(name = "order_dish_cancel_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long orderDishCancelId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_dish_id")
	private OrderDish orderDish;
	
	@Column(name="quantity_cancel")
	private Integer quantityCancel;
	
	@Column(name="comment_cancel")
	private String commentCancel;
	
	@Column(name="cancel_by")
	private String cancelBy;
	
	@Column(name="cancel_date")
	private Timestamp cancelDate;
}
