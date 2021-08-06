package fu.rms.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fu.rms.security.service.JwtUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtTokenUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

	public static String generateJwtToken(JwtUserDetails jwtUserDetail) {
		String token = Jwts.builder().setSubject(jwtUserDetail.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 60 * 24))
				.signWith(SignatureAlgorithm.HS512, "KeyBiMat").claim("role", jwtUserDetail.getAuthorities()).compact();
		return token;
	}

	public static String getUsernameByJwtToken(String token) {
		return Jwts.parser().setSigningKey("KeyBiMat").parseClaimsJws(token).getBody().getSubject();
	}

	public static boolean validateJwtToken(String token) {
		try {
			Jwts.parser().setSigningKey("KeyBiMat").parseClaimsJws(token).getBody();
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature. ", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token.", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired. }", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported. ", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty. ", e.getMessage());
		}
		return false;
	}

}
