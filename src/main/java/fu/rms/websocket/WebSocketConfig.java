package fu.rms.websocket;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import fu.rms.security.jwt.JwtAuthenFilter;
import fu.rms.security.service.JwtUserDetails;
import fu.rms.security.service.JwtUserDetailsService;
import fu.rms.utils.JwtTokenUtils;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Autowired
	private JwtUserDetailsService jwtUserDetailService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenFilter.class);

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/rms-websocket").setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				try {

					StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
							StompHeaderAccessor.class);
					if (StompCommand.CONNECT.equals(accessor.getCommand())) {

						List<String> tokenList = accessor.getNativeHeader("token");
						String token = tokenList.get(0);
						if (StringUtils.hasText(token) && JwtTokenUtils.validateJwtToken(token)) {
							logger.info("Valid token");
							String username = JwtTokenUtils.getUsernameByJwtToken(token);
							JwtUserDetails jwtUserDetail = jwtUserDetailService.loadUserByUsername(username);
							// check user exists
							if (jwtUserDetail != null) {
								UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
										jwtUserDetail, null, jwtUserDetail.getAuthorities());
								accessor.setUser(usernamePasswordAuthenticationToken);

							}
						}

					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				return message;
			}
		});

	}

}
