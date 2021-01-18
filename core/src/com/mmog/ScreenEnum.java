package com.mmog;

public enum ScreenEnum {
	
	MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainScreen();
        }
    },
	ADMIN_TASK {
        public AbstractScreen getScreen(Object... params) {
            return new AdminTaskScreen();
        }
    },
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen();
        }
    };
 
    public abstract AbstractScreen getScreen(Object... params);
}
