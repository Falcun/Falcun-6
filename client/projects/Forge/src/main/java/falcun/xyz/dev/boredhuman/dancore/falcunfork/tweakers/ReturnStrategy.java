package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/tweaker/ReturnStrategy.java")
public class ReturnStrategy {

	public Action action;
	public Object returnObject;

	public ReturnStrategy() {
		this.action = Action.DONOTHING;
	}

	public ReturnStrategy(Object returnObject) {
		this.action = Action.RETURNNOW;
		this.returnObject = returnObject;
	}

	public enum Action {
		DONOTHING, RETURNNOW;
	}
}
