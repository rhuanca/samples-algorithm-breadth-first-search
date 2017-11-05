import java.util.ArrayList;

public class Person {
	Integer id;
	String name;
	ArrayList<Integer> friends;

	public Person(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.friends = new ArrayList<>();
	}

	public void addFriendId(Integer friendId) {
		friends.add(friendId);
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", friends=" + friends + "]";
	}
}
