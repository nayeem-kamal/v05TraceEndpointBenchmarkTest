package v05Test;
import datadog.trace.api.Trace;
import java.util.concurrent.TimeUnit;


public class Main {

	public static void main(String[] args) {
		String load=args[0];
		
		String traceCount = args[1];
		int x = Integer.parseInt(traceCount);
		//System.out.println(x);
		long time = System.nanoTime();
		long sentTraces=0;
		if(load.equals("high")) {
			sentTraces=highLoadTest(x);
		}else if(load.equals("low")) {
			sentTraces=lowLoadTest(x);
		}else if(load.equals("variable")) {
			
		}
		
		
		long end = (System.nanoTime()-time);
		System.out.println("Sent "+sentTraces+" traces in " +TimeUnit.SECONDS.convert(end,TimeUnit.NANOSECONDS) +" seconds." );
		// TODO Auto-generated method stub
	}
	@Trace
	public static void sampleTrace(int x) {
		int y =x+x;
	}
	public static long highLoadTest(int count) {
		long traceCount = 0;
		for(int j = 0; j <30 ; j++) {
		for(long i = 0 ; i < count;i++) {
			sampleTrace(1);
			traceCount++;
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		}
		return traceCount;
	}
	public static long lowLoadTest(int count) {
		long sentTraces = 0;
		for(int i = 0; i<60;i++) {
			for(int j =0;j<count/60;j++) {
				sampleTrace(1);
				sentTraces++;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sentTraces;
		
	}

}
