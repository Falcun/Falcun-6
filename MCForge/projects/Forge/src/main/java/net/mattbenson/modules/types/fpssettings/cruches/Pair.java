package net.mattbenson.modules.types.fpssettings.cruches;

public class Pair<A, B> {
	A component1;
	B component2;
	
	public Pair(A component1, B component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	public A component1() {
		return component1;
	}
	
	public B component2() {
		return component2;
	}
}
