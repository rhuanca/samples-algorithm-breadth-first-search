import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {
	public static HashMap<Integer, Person> loadPeople(String filename) {
		HashMap<Integer, Person> map = new HashMap<>();

		// iterate all lines to get list of primary persons
		Integer counter = 1;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#"))
					continue; // it is a comment. do no read it.
				if (line.trim().isEmpty()) {
					continue;
				}
				
				String[] list = line.split("\\s+");
				
				String personName = list[0];
				Person person = lookupPerson(personName, map);
				if(person == null) {
					person = new Person(counter++, personName);
					map.put(person.id, person);
				}
				
				for (int i = 1; i < list.length; i++) {
					String friendName = list[i];
					Person friend = lookupPerson(friendName, map);
					if(friend == null) {
						friend = new Person(counter++, friendName);
						map.put(friend.id, friend);
					}
					person.addFriendId(friend.id);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to read:" + filename, e);
		}
		return map;
	}

	public static Person lookupPerson(String name, Map<Integer, Person> map) {
		for (Entry<Integer, Person> entry : map.entrySet()) {
			if (name.equals(entry.getValue().name)) {
				return entry.getValue();
			}
		}
		return null;
	}
}
