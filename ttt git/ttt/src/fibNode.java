final class fibNode implements Node {
	final private long value;
	private Node nextNode;
	
	
	private fibNode(long value, long previous, int index, int limit) {
		this.value = value;

		if (index < limit-1) {
			nextNode = new fibNode(previous+value, value, index+1, limit);
		} else {
			nextNode = null;
		}
	}

	private fibNode(Node that) {
		this.value = that.getValue();
		this.nextNode = that.next();
	}

	public long getValue() {
		return value;
	}

	@Override
	public synchronized Node next() {
		return nextNode.clone();
	}

	@Override
	public synchronized void setNext(Node a) {
		nextNode = a.clone(); 
	}
	
	public static Node newInstance(int limit){
		return new fibNode(0, 1, 0, limit);
	}

	@Override
	public Node clone() {
		return new fibNode(this);
	}

}
