package fu.rms.respone;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRespone<T>{

	private Integer page;
	
	private Integer totalPages;
	
	private List<T> result;
}
