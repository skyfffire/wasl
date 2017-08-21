import java.util.Date;
import java.text.SimpleDateFormat;

public class PrintTime {
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		System.out.println(sdf.format(new Date(System.currentTimeMillis())));
	}
}