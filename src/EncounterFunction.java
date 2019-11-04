@FunctionalInterface
public interface EncounterFunction {	//represents an action that the player can perform during an Encounter
	public void op(String s, Encounter e);	//player input, current Encounter
}
