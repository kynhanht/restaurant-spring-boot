package fu.rms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dishes")
@Getter
@Setter
public class Dish extends Auditable {

	@Id
	@Column(name="dish_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long dishId;
	
	@Column(name="dish_code")
	private String dishCode;
	
	@Column(name="dish_name")
	private String dishName;
	
	@Column(name="dish_unit")
	private String dishUnit;
	
	@Column(name="default_price")
	private Double defaultPrice;	// giá bán
	
	@Column(name="cost")
	private Double cost;			// chi phí nguyên vật liệu
	
	@Column(name="dish_cost")
	private Double dishCost;		// chi phí giá thành sản phẩm
	
	@Column(name="description",columnDefinition = "TEXT")
	private String description;
	
	@Column(name="time_complete")
	private Float timeComplete;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@Column(name="type_return")
	private Boolean typeReturn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;

	@ManyToMany
	@JoinTable(name="dish_category",
	joinColumns = @JoinColumn(name="dish_id"),
	inverseJoinColumns = @JoinColumn(name="category_id"))
	private List<Category> categories;
	
	@ManyToMany
	@JoinTable(name="dish_option",
	joinColumns = @JoinColumn(name="dish_id"),
	inverseJoinColumns = @JoinColumn(name="option_id"))
	private List<Option> options;
	
	@OneToMany(mappedBy = "dish",cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
	private List<Quantifier> quantifiers;
}
