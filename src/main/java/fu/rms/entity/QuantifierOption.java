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
@Table(name = "quantifier_option")
@Getter
@Setter
public class QuantifierOption {

	@Id
	@Column(name="quantifier_option_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long quantifierOptionId;
	
	@Column(name="quantity")
	private Double quantity;
	
	@Column(name="cost")
	private Double cost;
	
	@Column(name="description")
	private String description;
		
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
	private Option option;
	
}
