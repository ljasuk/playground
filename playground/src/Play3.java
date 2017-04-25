import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Scanner;

class Putter implements Runnable {

	private final BlockingQueue<Number> fQ;// = new ArrayBlockingQueue<Number>(256);
	private final int limit;
	private final int sleepNano;

	private void calcFib(long num1, long num2, int index)
			throws InterruptedException {
		if (index < limit) {
			fQ.put(num1 + num2);
			Thread.sleep(sleepNano);
			calcFib(num2, num1 + num2, index + 1);
		}
	}

	public Putter(BlockingQueue<Number> fQ, int limit, int sleepSec) {
		// super();
		this.fQ = fQ;
		this.limit = limit;
		sleepNano = sleepSec*1000;
	}

	@Override
	public void run() {
		try {
			fQ.put(0);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			calcFib(1, 0, 1);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

class Taker implements Runnable{
	private final BlockingQueue<Number> fQ;
	private final int limit;

	public Taker(BlockingQueue<Number> fQ, int limit) {
		this.fQ = fQ;
		this.limit = limit;
	}



	@Override
	public void run() {
		for(int i=0;i<limit;i++){
			try {
				System.out.println(fQ.take());
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
};

class Play3 {
	private static BlockingQueue<Number> fibi;
	private static int length;
	
	public static void main(String[] args){
		System.out.println("Length: ");
		Scanner scanner = new Scanner(System.in);
		length = scanner.nextInt();
		System.out.println("SelepSec: ");
		int sleepSec = scanner.nextInt();
		scanner.close();
		fibi = new ArrayBlockingQueue<Number>(length);
		new Thread(new Putter(fibi, length, sleepSec)).start();
		new Thread(new Taker(fibi, length)).start();
	}

}