package com.mmog.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.mmog.Client;
import com.mmog.players.CrewMember;

public class ElecTask extends Task {
    final static String taskName = "Elec Task";

    Stage stage;
    Sprite BNormal, BHover, BPress;
    final Image bNormalImg, bHoverImg, bPressImg;


    public ElecTask() {
        super(taskName);

        //skip nodes for now - since i need multiple copies of it and have to design pseudo code to iterate seamlessly

        stage = new Stage();

        BNormal = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi-lever.png"));
        BHover = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_bg.png"));
        BPress = new Sprite(new Texture("TaskUI/Reset Modem/panel_wifi_lidside.png"));

        //Resizing because of camera zoom
        //wifiLever.setSize(0, 0);
        //wifiPanel.setSize(0, 0);
        //sideLid.setSize(0, 0);

        bNormalImg = new Image(BNormal);
        bHoverImg = new Image(BHover);
        bPressImg = new Image(BPress);

        stage.addActor(bNormalImg);
        stage.addActor(bHoverImg);
        stage.addActor(bPressImg);

        bNormalImg.setPosition(stage.getWidth() /2 - 200, stage.getHeight() / 2 - 250);
        bHoverImg.setPosition(stage.getWidth() /2 + 75, stage.getHeight() / 2 + 135);
        bPressImg.setPosition(stage.getWidth() /2 - 390, stage.getHeight() / 2 - 290);

        bNormalImg.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchDragged(InputEvent e, float x, float y, int pointer) {
                bNormalImg.setPosition(bNormalImg.getX(), bNormalImg.getY() - y);

            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }

        });
    }


    public void render(Batch batch) {
        ((CrewMember) Client.getPlayer()).draw(batch);
        Gdx.input.setInputProcessor(stage);
        stage.draw();

		/*
		if(completed) {
			System.out.println("SUCCESS!");
			((CrewMember) Client.getPlayer()).setCurrentTask(null);
			((CrewMember) Client.getPlayer()).setTaskCompleted(taskName);
		}

		*/
    }

}
