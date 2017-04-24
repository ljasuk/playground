interface Node{
	public Node next();
	public void setNext(Node a);
	public Node clone();
	long getValue();
}

interface eval{
	boolean evaluate(Node a);
}

class nodeFilter{
	private final Node firstNode;
	private final eval e;
	private final Node filteredFirst;
	
	
	private nodeFilter(Node firstNode, eval e){
		this.firstNode = firstNode.clone();
		this.e = e;
		filteredFirst = filterList();
	}
	
	private Node filterList(){	
		if (firstNode==null||e==null) {
			throw new NullPointerException("invalid list or evaluator");
		}
		
		Node current = firstNode;
		Node newFirst = null;
		Node lastTrue = null;
		
		while(current != null){
			if (e.evaluate(current)){
				if (newFirst == null){
					newFirst=current;
				} else {				
					lastTrue.setNext(current);}
				lastTrue=current;
			}			
			current = current.next();
		}
		return newFirst;
	}

	synchronized Node getFilteredFirst() {
		return filteredFirst.clone();
	}
	
	synchronized static nodeFilter newInstance(Node firstNode, eval e){
		return new nodeFilter(firstNode, e);
	}
}