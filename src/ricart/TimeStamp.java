package ricart;
import java.util.ArrayList;
import java.util.Date;


public class TimeStamp {
	public synchronized static long getTimestamp()
	{
		// TS in long format, TimeStamp format
		// was generating error
		return new Date().getTime();
	}
	
	public static void main(String[] args)
	{
		// TEST CODE
		 System.out.println(new Date().getTime());
		 try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		 System.out.println(new Date().getTime());
		 ArrayList<String> lol = new ArrayList<String>();
		 lol.add("3");
		 lol.add("4");
		 System.out.println(lol.contains("3"));
	}
}
