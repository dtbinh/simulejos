package it.uniroma1.di.simulejos;

import java.awt.Frame;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

public final class Simulation implements Serializable {
	private static final long serialVersionUID = -290517947218502549L;

	private final List<Robot> robots = new LinkedList<>();

	private transient volatile Frame parentWindow;
	private transient volatile PrintWriter logWriter;
	private transient volatile boolean dirty = false;

	public Simulation(Frame parentWindow, Writer logWriter) {
		this.parentWindow = parentWindow;
		this.logWriter = new PrintWriter(new PartialWriter("Simulation",
				logWriter));
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clearDirty() {
		dirty = false;
	}

	public void addRobot(File classPath, String mainClassName, String script) {
		dirty = true;
		robots.add(new Robot(classPath, mainClassName, script, parentWindow,
				logWriter));
	}

	interface State {
		State play();

		State suspend();

		State stop();
	};

	private final State runningState = new State() {
		@Override
		public State play() {
			return this;
		}

		@Override
		public State suspend() {
			for (Robot robot : robots) {
				robot.suspend();
			}
			logWriter.println("suspended");
			return suspendedState;
		}

		@Override
		public State stop() {
			for (Robot robot : robots) {
				robot.stop();
			}
			logWriter.println("stopped");
			return stoppedState;
		}
	};

	private final State suspendedState = new State() {
		@Override
		public State play() {
			logWriter.println("resumed");
			for (Robot robot : robots) {
				robot.resume();
			}
			return runningState;
		}

		@Override
		public State suspend() {
			return this;
		}

		@Override
		public State stop() {
			for (Robot robot : robots) {
				robot.stop();
			}
			logWriter.println("stopped");
			return stoppedState;
		}
	};

	private final State stoppedState = new State() {
		@Override
		public State play() {
			logWriter.println("started");
			for (Robot robot : robots) {
				robot.play();
			}
			return runningState;
		}

		@Override
		public State suspend() {
			return this;
		}

		@Override
		public State stop() {
			return this;
		}
	};

	private State state = stoppedState;

	public void play() {
		state = state.play();
	}

	public void suspend() {
		state = state.suspend();
	}

	public void stop() {
		state = state.stop();
	}
}