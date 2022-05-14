package mapwriter;

import mapwriter.tasks.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import java.util.concurrent.*;

import org.lwjgl.opengl.Display;

import java.util.*;

public class BackgroundExecutor
{
    private final ExecutorService executor;
    private final LinkedList<Task> taskQueue;
    public boolean closed;
    private boolean doDiag;
    
    public BackgroundExecutor() {
        this.closed = false;
        this.doDiag = true;
        this.executor = Executors.newSingleThreadExecutor();
        this.taskQueue = new LinkedList<Task>();
    }
    
    public boolean addTask(final Task task) {
        if (!this.closed) {
            if (!task.CheckForDuplicate()) {
                final Future<?> future = this.executor.submit(task);
                task.setFuture(future);
                this.taskQueue.add(task);
            }
            if (this.tasksRemaining() > 500 && this.doDiag) {
                this.doDiag = false;
                this.taskLeftPerType();
            }
            else {
                this.doDiag = true;
            }
        }
        return this.closed;
    }
    
    public boolean processTaskQueue() {
        boolean processed = false;
        final Task task = this.taskQueue.poll();
        if (task != null) {
            if (task.isDone()) {
                task.printException();
                task.onComplete();
                processed = true;
            }
            else {
                this.taskQueue.push(task);
            }
        }
        return !processed;
    }
    
    public boolean processRemainingTasks(int attempts, final int delay) {
        while (this.taskQueue.size() > 0 && attempts > 0) {
            if (this.processTaskQueue()) {
                try {
                    Thread.sleep(delay);
                }
                catch (Exception ex) {}
                --attempts;
            }
        }
        return attempts <= 0;
    }
    
    public int tasksRemaining() {
        return this.taskQueue.size();
    }
    
    public boolean close() {
        boolean error = true;
        try {
            this.taskLeftPerType();
            this.executor.shutdown();
            this.processRemainingTasks(50, 5);
            error = !this.executor.awaitTermination(10L, TimeUnit.SECONDS);
            error = false;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.closed = true;
        return error;
    }
    
	private void taskLeftPerType() {
		HashMap<String, Object> tasksLeft = new HashMap<String, Object>();

		for (Task t : this.taskQueue) {
			String className = t.getClass().toString();
			if (tasksLeft.containsKey(className)) {
				tasksLeft.put(className,
						((Integer) tasksLeft.get(className)) + 1);
			} else {
				tasksLeft.put(className, 1);
			}
		}

		for (Map.Entry<String, Object> entry : tasksLeft.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
		}
	}
}
