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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "options")
@Getter
@Setter
public class Option extends Auditable{

	@Id
	@Column(name="option_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long optionId;
	
	@Column(name="option_name")
	private String optionName;
	
	@Column(name="option_type")
	private String optionType;
	
	@Column(name="unit")
	private String unit;
	
	@Column(name="price")
	private Double price;
	
	@Column(name = "cost")
	private Double cost;
	
	@Column(name="option_cost")
	private Double optionCost;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;
	
	@ManyToMany(mappedBy = "options")
	private List<Dish> dishes;

	@OneToMany(mappedBy = "option", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
	private List<QuantifierOption> quantifierOptions;
	
}
