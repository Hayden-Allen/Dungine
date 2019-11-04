
public class Player extends Character {	//special Character
	public Player(Game g, GameObject go) {
		super(go);
		g.setPlayer(this);	//automatically set as Game's player
	}
	
	public void interact(Room r, Character c) {	//TODO
		
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);	//send to Character
		GameObject visual = go.object("visual");
		//for Players, room coordinates represent world coordinates because players are always displayed in the center of a Room
		setWx(visual.attribute("x"));
		setWy(visual.attribute("y"));
	}
	public void print() {	//print formatted stats String. Bonuses from equipped Items are displayed alongside totals
		char open = Console.rgraphic("stats.open"), close = Console.rgraphic("stats.close");
		char value = Console.rgraphic("stats.separator.value"), stat = Console.rgraphic("stats.separator.stat");
		char bonusOpen = Console.rgraphic("stats.bonus.open"), bonusClose = Console.rgraphic("stats.bonus.close");
		char bonusPos = Console.rgraphic("stats.bonus.positive"), bonusNeg = Console.rgraphic("stats.bonus.negative");
		String currencyName = Console.rsetting("currency.name"), currencySuffix = Console.rsetting("currency.suffix");
		String name = Console.rgraphics("stats.name"), hp = Console.rgraphics("stats.hp"), atk = Console.rgraphics("stats.atk");
		String def = Console.rgraphics("stats.def"), spd = Console.rgraphics("stats.spd"), worth = Console.rgraphics("stats.worth");
		
		int datk = atk() - stats().atk(), ddef = def() - stats().def(), dspd = spd() - stats().spd();
		
		Console.logn("%c %s%c %s%c %s%c %,d%c %s%c %,d %c%c%,d%c%c %s%c %,d %c%c%,d%c%c %s%c %,d %c%c%,d%c%c %s%c %,d%s%c %s%c %,d%s %c",
				open,
				name, value, name(), stat,
				hp, value, hp(), stat,
				atk, value, atk(), bonusOpen, (datk < 0 ? bonusNeg : bonusPos), Math.abs(datk), bonusClose, stat,
				def, value, def(), bonusOpen, (ddef < 0 ? bonusNeg : bonusPos), Math.abs(ddef), bonusClose, stat,
				spd, value, spd(), bonusOpen, (dspd < 0 ? bonusNeg : bonusPos), Math.abs(dspd), bonusClose, stat,
				currencyName, value, gold(), currencySuffix, stat,
				worth, value, gold() + inv().value(), currencySuffix, close);
	}
}
