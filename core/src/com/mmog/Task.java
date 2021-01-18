package com.mmog;

public class Task {

	private String taskName;
	private boolean isCompleted;
	
	public Task(String taskName) {
		this.setTaskName(taskName);
		this.isCompleted = false;
	}
	
	public void setTaskCompleted() {
		this.isCompleted = true;
	}
	
	public boolean isTaskCompleted() {
		return isCompleted;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
