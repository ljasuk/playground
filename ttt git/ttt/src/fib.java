class fib{
	
	
	public static void main(String args[]){
		String s = "";
		int limit = 1500;
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < limit; i++) {
			s = Fibonacci.newInstance(limit).toString();
		}
		System.out.println(System.currentTimeMillis()-startTime);
		//System.out.println(Fibonacci.newInstance(50));
	}
}