final class Fibonacci {
	final private int size;
	final private Number first;

	private final class Number {
		private final int value;
		// private final int index;
		private final Number next;

		private Number(int previous, int value, int index) {
			// this.index = index;
			this.value = value;
			next = (index + 1 < size) ? 
					new Number(value, value + previous, index + 1)	:	null;
		}

	}

	private Fibonacci(int size) {
		this.size = size;
		first = new Number(1, 0, 0);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Number cursor = first;
		while (cursor.next != null) {
			sb.append(cursor.value + " ");
			cursor = cursor.next;
		}
		sb.append(cursor.value);
		return sb.toString();
	}

	public static Fibonacci newInstance(int s) {
		return new Fibonacci(s);
	}

}