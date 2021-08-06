package fu.rms.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fu.rms.constant.AppMessageErrorConstant;

public class Utils {

	/*
	 * tự sinh mã order
	 */
	public static String generateOrderCode() {

		String newOrderCode = "RMS";
		newOrderCode += new SimpleDateFormat("yyMMdd").format(new Date());
		newOrderCode += "-" + randomAlphaNumberic(6);
		return newOrderCode;
	}

	/*
	 * tự sinh mã staff
	 */
	public static String generateStaffCode(String fullName) {

		fullName = fullName.trim().replaceAll("\\s+", " ");
		String engFullName = vieToEng(fullName);
		String words[] = engFullName.split(" ");
		StringBuilder staffCode = new StringBuilder();

		if (words.length > 1) {
			staffCode.append(words[words.length-1].toString());
			for (int i = 0; i < words.length-1; i++) {
				staffCode.append(words[i].charAt(0));
			}
		}else {
			staffCode.append(words[0].toString());
		}
		
		return staffCode.toString().toUpperCase();
	}
	
	private static String vieToEng(String str) {
		String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d");
    }

	public static String encodePassword(String password) {	
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		String encodedPassword = passwordEncoder.encode(password);
		return encodedPassword;
	}

	/*
	 * tự sinh mã staff
	 */
	private static String randomAlphaNumberic(int number) {
		Random rd = new Random();

		StringBuilder sb = new StringBuilder(number);
		for (int i = 0; i < number; i++) {
			sb.append(AppMessageErrorConstant.ALPHA_NUMBERIC
					.charAt(rd.nextInt(AppMessageErrorConstant.ALPHA_NUMBERIC.length())));
		}

		return sb.toString();
	}

	/*
	 * tính thời gian order theo ngày/giờ/phút. VD: 2 giờ 24p
	 */
	public static String getOrderTime(Timestamp currentTime, Timestamp orderTime) {
		String timeOrder = "";

		long diffSeconds = (currentTime.getTime() - orderTime.getTime()) / 1000;
		if (diffSeconds < 0)
			return null;
		if (diffSeconds >= 86400) {
			long diffDays = diffSeconds / (24 * 60 * 60);
			timeOrder += String.valueOf(diffDays) + " ngày ";
			diffSeconds = diffSeconds - (24 * 60 * 60 * diffDays);
		}

		if (diffSeconds >= 3600) {
			long diffHours = diffSeconds / (60 * 60);
			timeOrder += String.valueOf(diffHours) + " giờ ";
			diffSeconds = diffSeconds - (60 * 60 * diffHours);
		}

		if (diffSeconds >= 60 && !timeOrder.contains("ngày")) {
			long diffMinutes = diffSeconds / (60);
			timeOrder += String.valueOf(diffMinutes) + " phút";
			diffSeconds = diffSeconds - (60 * 60 * diffMinutes);
		}
		if (timeOrder.equals("")) {
			timeOrder += String.valueOf(diffSeconds) + " giây";
		}

		return timeOrder.trim();
	}

	public static boolean getTimeToNotification(Timestamp orderTime, Float timeToComplete) {
		Float time = null;
		if (timeToComplete == null || timeToComplete == 0)
			return false;

		long diffSeconds = (getCurrentTime().getTime() - orderTime.getTime()) / 1000;
		long diffMinutes = diffSeconds >= 60 ? diffSeconds / 60 : diffSeconds % 60;
		time = (float) diffMinutes;

		if (time >= timeToComplete * 1.2)
			return true; // bếp chậm việc
		return false;
	}

	/*
	 * lấy thời gian hiện tại
	 */
	public static Timestamp getCurrentTime() {
		Date date = new Date();
		return new Timestamp(date.getTime());
	}

	/*
	 * tự sinh mã import
	 */
	public static String generateImportCode() {

		String newImportCode = "RMS-NK00";
		newImportCode += new SimpleDateFormat("MMdd").format(new Date());
		newImportCode += "-" + randomAlphaNumberic(3);
		return newImportCode;
	}

	/*
	 * tự sinh mã export
	 */
	public static String generateExportCode() {

		String newExportCode = "RMS-XK00";
		newExportCode += new SimpleDateFormat("MMdd").format(new Date());
		newExportCode += "-" + randomAlphaNumberic(3);
		return newExportCode;
	}

	/*
	 * tự sinh mã import, exort code cho kiểm kê
	 */
	public static String generateInventoryImExCode(String inventoryCode) {

		String newExportCode = "RMS-KK00";
		newExportCode += new SimpleDateFormat("MMdd").format(new Date());
		newExportCode += "-" + inventoryCode;
		return newExportCode;
	}

	public static String generateDuplicateCode(String code) {

		StringBuilder sb = new StringBuilder(code);
		StringBuilder numberOfDuplicate = new StringBuilder();
		for (int i = sb.length() - 1; i >= 0; i--) {
			if (Character.isDigit(sb.charAt(i))) {
				numberOfDuplicate.append(sb.charAt(i));
				sb.deleteCharAt(i);
			} else {
				break;
			}
		}

		if (numberOfDuplicate.length() == 0) {
			numberOfDuplicate.append(0);
		} else {
			numberOfDuplicate.reverse();
		}
		sb.append(Integer.parseInt(numberOfDuplicate.toString()) + 1);
		return sb.toString();
	}

	public static Double roundUpDecimal(Double decimal) {
		if (decimal == null)
			return null;
		decimal = Math.ceil(decimal); // làm tròn double
		return decimal;
	}

	public static Double sumBigDecimalToDouble(Double d1, Double d2) { // cộng 2 số double
		if (d1 == null || d2 == null)
			return null;
		BigDecimal bd1 = BigDecimal.valueOf(d1);
		BigDecimal bd2 = BigDecimal.valueOf(d2);
		BigDecimal sum = bd1.add(bd2).setScale(3, BigDecimal.ROUND_HALF_EVEN);
		return sum.doubleValue();
	}

	public static Double subtractBigDecimalToDouble(Double d1, Double d2) { // trừ 2 số double
		if (d1 == null || d2 == null)
			return null;
		BigDecimal bd1 = BigDecimal.valueOf(d1);
		BigDecimal bd2 = BigDecimal.valueOf(d2);
		BigDecimal minus = bd1.subtract(bd2).setScale(3, BigDecimal.ROUND_HALF_EVEN);
		return minus.doubleValue();
	}

	public static Double multiBigDecimalToDouble(Double d1, Double d2) { // nhân 2 số double
		if (d1 == null || d2 == null)
			return null;
		BigDecimal bd1 = BigDecimal.valueOf(d1);
		BigDecimal bd2 = BigDecimal.valueOf(d2);
		BigDecimal multi = bd1.multiply(bd2).setScale(3, BigDecimal.ROUND_HALF_EVEN);
		return multi.doubleValue();
	}

	public static Double divideBigDecimalToDouble(Double d1, Double d2) { // chia 2 số double
		if (d1 == null || d2 == null)
			return null;
		BigDecimal bd1 = BigDecimal.valueOf(d1);
		BigDecimal bd2 = BigDecimal.valueOf(d2);
		BigDecimal divide = bd1.divide(bd2, 3, BigDecimal.ROUND_HALF_EVEN);
		return divide.doubleValue();
	}

	public static Double bigDecimalToDouble(Double d1) { // chia 2 số double
		if (d1 == null)
			return null;
		BigDecimal bd1 = BigDecimal.valueOf(d1);
		BigDecimal bd2 = BigDecimal.valueOf(0d);
		BigDecimal sum = bd1.add(bd2).setScale(3, BigDecimal.ROUND_HALF_EVEN);
		return sum.doubleValue();
	}

}
