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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long orderId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_taker_id")
	private Staff orderTakerStaff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="chef_id")
	private Staff chefStaff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cashier_id")
	private Staff cashierStaff;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="table_id")
	private Tables table;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="total_amount")
	private Double totalAmount;
	
	@Column(name="total_item")
	private Integer totalItem;
	
	@Column(name="customer_payment")
	private Double customerPayment;
	
	@Column(name="order_date")
	private Timestamp orderDate;
	
	@Column(name="payment_date")
	private Timestamp paymentDate;
	
	@Column(name="modified_date")
	private Timestamp modifiedDate;
	
	@Column(name="create_by")
	private String createBy;
	
	@Column(name="modified_by")
	private String modifiedBy;
	
	@Column(name="time_to_complete")
	private String timeToComplete;
	
	@OneToMany(mappedBy = "order")
	private List<OrderDish> orderDish;
	
	
}
