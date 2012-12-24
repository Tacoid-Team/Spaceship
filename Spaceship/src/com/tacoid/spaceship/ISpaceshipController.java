package com.tacoid.spaceship;

public interface ISpaceshipController {
	public void engineLeft(boolean on);
	public void engineRight(boolean on);
	public void engineBoth(boolean b);
	public boolean isLeftOn();
	public boolean isRightOn();
	public boolean areBothOn();
}
