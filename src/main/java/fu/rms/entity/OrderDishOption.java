package fu.rms.entity;


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
@Table(name = "order_dish_option")
@Getter
@Setter
public class OrderDishOption {

	@Id
	@Column(name="order_dish_option_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long orderDishOptionId;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="unit_price")
	private Double unitPrice;
	
	@Column(name="sum_price")
	private Double sumPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="option_id")
	private Option option;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_dish_id")
	private OrderDish orderDish;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;
}
