import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BreadthFirstSearch {

	public static void main(String args[]) {
		System.out.println("Program arguments: " + args);
		String filename = args[0];
		int source = Integer.parseInt(args[1]);
		int destination = Integer.parseInt(args[2]);

		HashMap<Integer, Person> people = Utils.loadPeople(filename);

		System.out.println("source: " + people.get(source));
		System.out.println("dest: " + people.get(destination));
		
//		LinkedList<Person> persons = findPathBiBFS(people, source, destination);
//		System.out.println("path:");
//		for(Person person: persons) {
//			System.out.println("\t"+person.name);
//		}
	}

	public static LinkedList<Person> findPathBiBFS(HashMap<Integer, Person> people, Integer source, Integer destination) {
		BFSData sourceData = new BFSData(people.get(source));
		BFSData destData = new BFSData(people.get(destination));
		
		while(!sourceData.isFinished() && !destData.isFinished()) {
			/* search out from source */
			Person collision = searchLevel(people, sourceData, destData);
			
			System.out.println(">>> collision1 = " + collision);
			
			if(collision!=null) {
				return mergePaths(sourceData, destData, collision.id);
			}
			
			/* search out from destination */
			collision = searchLevel(people, destData, sourceData);
			System.out.println(">>> collision2 = " + collision);
			
			if(collision != null) {
				return mergePaths(sourceData, destData, collision.id);
			}
		}
		
		return null;
	}

	private static Person searchLevel(HashMap<Integer, Person> people, BFSData primary, BFSData secondary) {
		/*
		 * We only want to search one level at a time, Count how many nodes are
		 * currently in the primary's level and only do that many nodes. We'll continue
		 * to add nodes to the end. 
		 */
		
		int count = primary.toVisit.size();
		for(int i=0; i < count; i++) {
			/* Pull out first node */
			PathNode pathNode = primary.toVisit.poll();
			int personId = pathNode.getPerson().id;
			
			/* check if it's already been visited */
			if(secondary.visited.containsKey(personId)) {
				return pathNode.getPerson();
			}
			
			/* add friends to queue */
			Person person  = pathNode.getPerson();
			ArrayList<Integer> friends = person.friends;
			for(Integer friendId: friends) {
				if(!primary.visited.containsKey(friendId)) {
					Person friend = people.get(friendId);
					PathNode next = new PathNode(friend, pathNode);
					primary.visited.put(friendId, next);
					primary.toVisit.add(next);
				}
			}
		}
		return null;
	}
	
	private static LinkedList<Person> mergePaths(BFSData bfs1, BFSData bfs2, Integer connection) {
		PathNode end1 = bfs1.visited.get(connection); // end1 -> source
		PathNode end2 = bfs2.visited.get(connection); // end2 -> dest
		LinkedList<Person> pathOne = end1.collapse(false);
		LinkedList<Person> pathTwo = end2.collapse(true); // reverse
		pathTwo.removeFirst(); // remove connection
		pathOne.addAll(pathTwo); // add second path
		return pathOne;
	}


}
