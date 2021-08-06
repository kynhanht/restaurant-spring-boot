package fu.rms.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "staffs")
@Getter
@Setter
public class Staff extends Auditable {

	@Id
	@Column(name="staff_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long staffId;
	
	@Column(name="staff_code")
	private String staffCode;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password",columnDefinition = "TEXT")
	private String password;
	
	@Column(name="full_name")
	private String fullname;
	
	@Column(name="staff_phone")
	private String phone;
	
	@Column(name="address")
	private String address;
	
	@Column(name="is_activated")
	private Integer isActivated;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	
	@OneToMany(mappedBy = "orderTakerStaff")
	private List<Order> orderTakerOrder;
	
	@OneToMany(mappedBy = "chefStaff")
	private List<Order> chefOrder;
	
	@OneToMany(mappedBy = "cashierStaff")
	private List<Order> cashierOrder;

	@Column(name="pass")
	private String pass;
	
	
}
