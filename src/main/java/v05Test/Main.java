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
			String tps = args[2];
			int tpsec=Integer.parseInt(tps);
			sentTraces=lowLoadTest(x,tpsec);
		}else if(load.equals("var")) {
			String tps = args[2];
			int tpsec=Integer.parseInt(tps);
			sentTraces=varLoadTest(x,tpsec);
		}
		
		
		long end = (System.nanoTime()-time);
		System.out.println("Sent "+sentTraces+" traces in " +TimeUnit.SECONDS.convert(end,TimeUnit.NANOSECONDS) +" seconds." );
	}
	@Trace(operationName = "Test", resourceName = "v04")
	public static void sampleTrace(int x) {
		int y =x+x;
	}
	public static long highLoadTest(int count) {
		long traceCount = 0;
		for(long i = 0 ; i < count;i++) {
			sampleTrace(1);
			traceCount++;
		}

		
		return traceCount;
	}
	public static long lowLoadTest(int count,int tpsec) {
		long sentTraces = 0;
		
		while(sentTraces<count) {
			long time = System.nanoTime();
			for(int j =0;j<tpsec&&j<=count;j++) {
				sampleTrace(1);
				sentTraces++;
			}
			long end = System.nanoTime()-time;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return sentTraces;
		
	}
	public static long varLoadTest(int count,int tpsec) {
		long sentTraces = 0;
		
		while(sentTraces<count) {
			long time = System.nanoTime();
			for(int j =0;j<tpsec&&j<=count;j++) {
				sampleTrace(1);
				sentTraces++;
			}
			long end = System.nanoTime()-time;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int j =0;j<tpsec/4&&j<=count;j++) {
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
