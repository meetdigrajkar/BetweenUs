package com.mmog;

public enum ScreenEnum {
	MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainScreen();
        }
    },
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen((Integer) params[0]);
        }
    };
 
    public abstract AbstractScreen getScreen(Object... params);
}
