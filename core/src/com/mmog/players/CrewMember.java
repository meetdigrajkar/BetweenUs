package com.mmog.players;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mmog.tasks.Task;

public class CrewMember extends Player {
	
	public boolean isDoingTask = false;
	private ArrayList<Task> tasks;
	private BitmapFont f;
	private Task currentTask;

	public CrewMember(int playerID) {
		super(playerID);
		this.tasks = new ArrayList<Task>();
		f = new BitmapFont();
		currentTask = null;
	}
	
	public void drawTasks(Batch batch) {
		f.draw(batch, tasksToString(), getX() - (Gdx.graphics.getWidth()/6) + 40  , getY() + Gdx.graphics.getHeight()/6 + 45);
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	public Task getCurrentTask() {
		return currentTask;
	}
	
	public void setCurrentTask(Task task) {
		this.currentTask = task;
	}
	
	/*
	 * Sets the current tasks of the crew member if the player collides with any of the tasks
	 */
	public void setCurrentTaskIfCollided() {
		for(Task task: tasks) {
			if(!task.isTaskCompleted() && checkCollisionOnTask(task.getTaskName())) {	
				currentTask = task;
				return;
			}
		}
	}
	
	public boolean checkCollisionOnTask(String taskName) {
		if((collisionAtX(1,taskName) || collisionAtY(1,taskName))){
			return true;
		}
		return false;
	}
	
	public boolean isTaskCompleted(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task) && t.isTaskCompleted()) {
				return true;
			}
		}
		return false;
	}

	
	public void setTaskCompleted(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task)) {
				t.setTaskCompleted();
				return;
			}
		}
	}

	public boolean hasTask(String task) {
		for(Task t: tasks) {
			if(t.getTaskName().equals(task)) {
				return true;
			}
		}
		return false;
	}

	public String tasksToString() {
		String tasksString = "-Crew Member Tasks-\n";

		for(Task t: tasks) {
			tasksString += t.getTaskName();

			if(t.isTaskCompleted())
				tasksString += ": COMPLETED\n";
			else
				tasksString += ": INCOMPLETE\n";
		}

		return tasksString;
	}
	

	public Task getTask(String taskName) {
		Task task = null;
		
		for(Task t: tasks) {
			if(t.getTaskName().equals(taskName)) {
				task = t;
			}
		}
		
		return task;
	}
	

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

}
