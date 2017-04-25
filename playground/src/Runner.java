import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

class Runner implements Runnable {
	private final String name;
	private Runner enemy;
	private double distance = 0;
	private final int finish;
	public int speed = 0;
	public Lock lock = new ReentrantLock();

	@Override
	public void run() {
		while (distance < finish) {
			System.out.printf("%s: %.2f m %d km/h\n", name, distance, speed);
			if (speed < 20)
				speed++;
			distance += (double) speed / 3.6;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			int a = ThreadLocalRandom.current().nextInt(6);
			if (a == 5 && enemy.lock.tryLock()) {
				enemy.speed = 0;
				System.err.println(name + ": I made "+enemy.getName()+" trip");
			}
		}
		System.out.println(name+": Now I know: "+Race.HolyGrail.getSecret());
	}

	public Runner(String name, int finish) {
		this.name = name;
		this.finish = finish;
	}

	public synchronized void setEnemy(Runner enemy) {
		this.enemy = enemy;
	}

	public synchronized String getName() {
		return name;
	}
	
	
	
};