package net.mattbenson.events.types.movement;

import net.mattbenson.events.Event;
import net.minecraft.util.MovementInput;

//Hooked at net.minecraft.util.MovementInputFromOptions.java
public class MovementInputEvent extends Event {
	private MovementInput movementInput;
	
	public MovementInputEvent(MovementInput movementInput) {
		this.movementInput = movementInput;
	}
	
	public MovementInput getMovementInput() {
		return movementInput;
	}
}
