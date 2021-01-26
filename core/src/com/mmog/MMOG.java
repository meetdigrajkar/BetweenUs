package com.mmog;

import com.badlogic.gdx.Game;
import com.mmog.screens.ScreenEnum;
import com.mmog.screens.ScreenManager;

public class MMOG extends Game {	

	@Override
	public void create () {
		//uncomment when refactored code in this class into seperate scenes
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
	}
	
	@Override
	public void dispose() {
		System.out.println("CLOSED!");
		Client.removeClient();
	}
}
