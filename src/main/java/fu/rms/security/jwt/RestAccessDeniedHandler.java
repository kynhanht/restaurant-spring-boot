package fu.rms.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fu.rms.constant.MessageErrorConsant;
import fu.rms.exception.handler.MessageError;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RestAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		logger.error(accessDeniedException.getMessage());	
		//set messageError
		MessageError messageError=new MessageError(HttpStatus.FORBIDDEN,MessageErrorConsant.ERROR_USER_FORBIDDEN);

		//response messageError to client
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
