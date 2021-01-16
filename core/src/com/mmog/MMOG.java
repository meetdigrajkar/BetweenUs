package com.mmog;

import com.badlogic.gdx.Game;

public class MMOG extends Game {	

	@Override
	public void create () {
		//uncomment when refactored code in this class into seperate scenes
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
	}
}
