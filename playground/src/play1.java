import java.util.*;

class play1 {
	static int raceTo;
	private static Thread t1;
	private static Thread t2;

	private static final Runnable TRY_OUT_THREAD = new Runnable() {
		public void run() {
			for (int i = 0; i < raceTo; i++) {
				System.out.println(i);
				try {
					Thread.sleep(10000 / raceTo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private static final Runnable TRY_OUT_THREAD2 = new Runnable() {
		public void run() {
			for (int i = 0; i < raceTo - 1; i++) {
				System.err.println(i);
				try {
					Thread.sleep(10000 / (raceTo - 1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	static class Waiter extends Thread {
		public void run() {
			// System.out.println(this.getClass()+" starts waiting");
			System.out.println(
					Thread.currentThread().getName() + " starts waiting");
			try {
				t1.join();
				System.out.println(t1.getName() + " is here");
				t2.join();
				System.out.println(t2.getName() + " is here");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		public static Waiter newInstance() {
			return new Waiter();
		}
	}

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName());
		System.out.println(System.currentTimeMillis());
		Scanner in = new Scanner(System.in);
		raceTo = in.nextInt() + 1;
		t1 = new Thread(TRY_OUT_THREAD);
		t2 = new Thread(TRY_OUT_THREAD2);
		t1.start();
		t2.start();
		new Waiter().start();
		in.close();

	}
}