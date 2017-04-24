final class FibValuator implements eval {
	final private long[] numbers;
	final private int length;
	
	

	FibValuator(int length) {
		this.length = length;
		numbers = new long[length];
		numbers[0] = 0;
		numbers[1] = 1;
		for (int i = 2; i < numbers.length; i++) {
			numbers[i] = numbers[i-2]+numbers[i-1];
			System.out.println(numbers[i]);
		}
	}



	@Override
	public boolean evaluate(Node a) {
		long value = a.getValue();
		int i = 0;
		long checknum;
		while (i<length){
			checknum = numbers[i];
			if (checknum<value) i++; else
			if (checknum == value) return true; else
			if (checknum > value) return false;
		}
		System.err.println("evaluator too short".toUpperCase());
		return false;
	}

}
