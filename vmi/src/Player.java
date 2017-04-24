import java.util.*;
import java.io.*;
//import java.math.*;

class Entity{
    final private int entityId;
    private enum entityType {BARREL, CANNONBALL, MINE, SHIP};
    final private int x;
    final private int y;
    final private int arg1;
    final private int arg2;
    final private int arg3;
    final private int arg4;
	
    private Entity(int entityId, String entityType, int x, int y, int arg1,
			int arg2, int arg3, int arg4) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
		this.x = x;
		this.y = y;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		this.arg4 = arg4;
	}
    
    public static Entity newInstance(Scanner in){
    	int entityId = in.nextInt();
        String entityType = in.next();
        int x = in.nextInt();
        int y = in.nextInt();
        int arg1 = in.nextInt();
        int arg2 = in.nextInt();
        int arg3 = in.nextInt();
        int arg4 = in.nextInt();
        return new Entity(entityId, entityType, x, y, arg1, arg2, arg3, arg4);
    }

	synchronized String getEntityType() {
		return entityType;
	}

	synchronized int getX() {
		return x;
	}

	synchronized int getY() {
		return y;
	}
    
    int dist(int a, int b){
    	int d = 0;
    	d+= Math.abs(a-x);
    	int hor = Math.abs(b-y);
    	if (hor>d/2) d+= hor-d/2;
    	return d;
    }
    
}

class Player {
	private static List<Entity> myShips = new ArrayList<Entity>();
	private static List<Entity> enemyShips = new ArrayList<Entity>();
	private static List<Entity> barrels = new ArrayList<Entity>();
	private static int myShipCount;
	private static int entityCount;
	
	private static void readData(Scanner in){
		myShipCount = in.nextInt(); // the number of remaining ships
        entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
        barrels.clear();
        myShips.clear();
        
        for (int i = 0; i < entityCount; i++) {
        	Entity read = Entity.newInstance(in);
        	//System.err.println(read.getEntityType());
        	switch (read.getEntityType()) {
        		case "BARREL":
        			barrels.add(read);
        			break;
        		case "SHIP":
        			if (i<myShipCount) myShips.add(read); else enemyShips.add(read);
        			break;
        	}
        }
	}
	
	private static Entity findClosest(Entity toShip){
		Entity closest = null;
		int a = toShip.getX();
		int b = toShip.getY();
		int bestDist=0;
		
		for (Entity thisBarrel : barrels) {
			int thisDist = thisBarrel.dist(a, b);
			if (closest == null||thisDist<bestDist) {
				closest = thisBarrel;
				bestDist = thisDist;
			}
		}
		return closest;
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            readData(in);
            
            //System.err.println(myShip.getX()+" "+myShip.getY());
            
            for (int i = 0; i < myShipCount; i++) {
            	
            	Entity target = findClosest(myShips.get(i));
                System.out.println("MOVE "+target.getX()+" "+target.getY()); // Any valid action, such as "WAIT" or "MOVE x y"
            }
        }
    }
}