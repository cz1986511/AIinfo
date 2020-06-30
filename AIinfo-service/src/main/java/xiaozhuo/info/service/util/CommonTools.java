package xiaozhuo.info.service.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class CommonTools {

	private CommonTools() {
		throw new IllegalStateException("Utility class");
	}

	public static String getDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new Date());
	}

	public static String getMonthString() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
			calendar.add(Calendar.MONTH, 1);
		}
		return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
	}

	public static String generateDbKey() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	public static Date getStringDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			log.error(e.toString());
		}
		return null;
	}

	public static Date getStringDateToS(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			log.error(e.toString());
		}
		return null;
	}

	public static Date getDateString(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != dateString) {
				return formatter.parse(dateString);
			}
		} catch (ParseException e) {
			log.error(e.toString());
		}
		return null;
	}

	public static WebClient initWebClient() {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		int timeout = webClient.getOptions().getTimeout();
		webClient.getOptions().setTimeout(timeout * 10);
		return webClient;
	}

}
