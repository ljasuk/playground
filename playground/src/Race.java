import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

class Grail{
	private final String secret;
	
	public Grail(String secret) {
		super();
		this.secret = secret;
	}

	public synchronized String getSecret() {
		return secret;
	}
	
}

final class Race {
	public static final Grail HolyGrail = new Grail("Ocra is dead");
	
	public static void main(String[] asdf) {
		
		Runner r2 = new Runner("Asimov", 10);
		Runner r1 = new Runner("Belfort", 10);
		r1.setEnemy(r2);
		r2.setEnemy(r1);

		//Thread t1 = new Thread(r1);
		//Thread t2 = new Thread(r2);
		
		//t1.start();
		//t2.start();
		
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
		
		pool.scheduleAtFixedRate(r1, (int)(Math.random()*3), 10, SECONDS);
		pool.scheduleAtFixedRate(r2, (int)(Math.random()*3), 10, SECONDS);
		
		
		
		//pool = ((ThreadPoolExecutor) pool);
		
		while (!pool.isTerminated()){
			
		}
		
		System.out.println("Race over");
		
		//System.out.print((t2.isAlive()) ? r1.getName() : r2.getName());
		//System.out.println(" won the Race!");
	}

}