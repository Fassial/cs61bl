/** Some coding styles I didn't notice before:
	*	File ends with a newline.
	*	File does not contain tab characters (this is the first instance) in the second line of a class. 
	*	The variable of the class should not be public.
	**	Definition of 'equals()' with corresponding definition of 'hashCode()'. [EqualsHashCode]
	*	'if' is followed by whitespace.
	*	'while' is followed by whitespace.
	**	Method name 'helper_method' must start with a lower-case letter, or consist of a single, upper-case letter.
	*	'typecast' is followed by whitespace.
	*	';' is followed by whitespace.
**/
public interface Deque<Item> {

	public void addFirst(Item item);
	public void addLast(Item item);
	public default boolean isEmpty() {
		return size() == 0;
	}
	public int size();
	public void printDeque();
	public Item removeFirst();
	public Item removeLast();
	public Item get(int index);
}
