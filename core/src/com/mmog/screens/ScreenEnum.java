package com.mmog.screens;

public enum ScreenEnum {
	
	MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainScreen();
        }
    },
	LOBBY_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new LobbyScreen();
        }
    },
	JOIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new JoinScreen();
        }
    },
	CREATE_ROOM {
        public AbstractScreen getScreen(Object... params) {
            return new CreateRoomScreen();
        }
    },
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen();
        }
    };
 
    public abstract AbstractScreen getScreen(Object... params);
}
