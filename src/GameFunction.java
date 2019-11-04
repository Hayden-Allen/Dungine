@FunctionalInterface
public interface GameFunction {	//represents an action that the player can perform
	public void op(String s, Game g);	//player input, Game that player belongs to
}
