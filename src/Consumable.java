
public class Consumable extends Item {	//a one-time-use Item. Not implemented yet
	private int duration, hp;	//how many turns (of an Encounter) the effects will last, health given to target
	private boolean self;	//whether or not this Consumable acts on the user or another target
	
	public Consumable(GameObject go) {
		super(go);
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);	//send to Item
		//get Consumable-specific values
		hp = go.attribute("hp");
		duration = go.attribute("duration");
		self = go.attribute("self");
	}
}
