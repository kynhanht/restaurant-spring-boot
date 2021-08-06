package fu.rms.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.exception.handler.MessageError;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		logger.error("Unauthorized error: "+authException.getMessage());
	   //set messageError
		MessageError messageError=new MessageError(HttpStatus.UNAUTHORIZED,MessageErrorConsant.ERROR_USER_UNAUTHORIZED);
		
		//response messageError to client
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.writeValueAsString(messageError);
		String jsonString=objectMapper.writeValueAsString(messageError);   
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(jsonString);
		out.flush();

		
	}
	  
	 
}
