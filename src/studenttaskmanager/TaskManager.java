package studenttaskmanager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class TaskManager {

    private final Map<Integer, Task> tasks = new LinkedHashMap<>();
    private int nextId = 1;

    public synchronized void addTask(String name)
            throws TaskException {

        if (name == null || name.trim().isEmpty()) {
            throw new TaskException(
                    "Please enter a task description.");
        }

        Task task = new Task(nextId++, name.trim());
        tasks.put(task.getId(), task);

        System.out.println("\nYour task has been added successfully!");
        System.out.println(task);
    }

    public synchronized void viewTasks() {

        if (tasks.isEmpty()) {
            System.out.println(
                    "\nYou haven't added any tasks yet.");
            return;
        }

        System.out.println("\n========== YOUR TASKS ==========");

        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public synchronized void searchTask(int id)
            throws TaskException {

        Task task = getTask(id);

        System.out.println("\nHere are your task details:");
        System.out.println(task);
    }

    public synchronized void completeTask(int id)
            throws TaskException {

        Task task = getTask(id);

        if (task.isCompleted()) {
            throw new TaskException(
                    "This task is already completed.");
        }

        task.markCompleted();

        System.out.println(
                "\nGreat job! Your task is now completed.");
    }

    public synchronized void deleteTask(int id)
            throws TaskException {

        if (tasks.remove(id) == null) {
            throw new TaskException(
                    "We couldn't find a task with ID " + id + ".");
        }

        System.out.println(
                "\nYour task has been deleted successfully.");
    }

    public synchronized void viewByStatus(boolean completed) {

        boolean found = false;

        if (completed) {
            System.out.println("\n========== COMPLETED TASKS ==========");
        } else {
            System.out.println("\n========== PENDING TASKS ==========");
        }

        for (Task task : tasks.values()) {
            if (task.isCompleted() == completed) {
                System.out.println(task);
                found = true;
            }
        }

        if (!found) {
            if (completed) {
                System.out.println(
                        "No completed tasks yet. Keep going!");
            } else {
                System.out.println(
                        "You're all caught up! No pending tasks.");
            }
        }
    }

    private Task getTask(int id) throws TaskException {

        Task task = tasks.get(id);

        if (task == null) {
            throw new TaskException(
                    "We couldn't find a task with ID " + id + ".");
        }

        return task;
    }

    public void processTasks() {

        List<Integer> pendingIds = new ArrayList<>();

        synchronized (this) {
            for (Task task : tasks.values()) {
                if (!task.isCompleted()) {
                    pendingIds.add(task.getId());
                }
            }
        }

        if (pendingIds.isEmpty()) {
            System.out.println(
                    "\nYou're all caught up! No pending tasks to process.");
            return;
        }

        System.out.println(
                "\nStarting task processing. Please wait...");

        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        List<Future<?>> futures = new ArrayList<>();

        try {
            for (int id : pendingIds) {

                Future<?> future = executor.submit(() -> {

                    String threadName =
                            Thread.currentThread().getName();

                    try {
                        System.out.println(
                                threadName
                                + " is working on task ID: " + id);

                        Thread.sleep(1000);

                        synchronized (TaskManager.this) {
                            Task task = getTask(id);

                            if (!task.isCompleted()) {
                                task.markCompleted();

                                System.out.println(
                                        threadName
                                        + " finished: "
                                        + task.getName());
                            }
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();

                        System.out.println(
                                threadName
                                + " was interrupted.");

                    } catch (TaskException e) {
                        System.out.println(e.getMessage());
                    }
                });

                futures.add(future);
            }

        } catch (RuntimeException e) {
            executor.shutdownNow();

            System.out.println(
                    "Unable to start task processing. Please try again.");
            return;
        }

        executor.shutdown();

        try {
            for (Future<?> future : futures) {
                future.get();
            }

            if (!executor.awaitTermination(
                    1, TimeUnit.MINUTES)) {

                executor.shutdownNow();

                System.out.println(
                        "Processing took too long. Please try again.");
                return;
            }

            System.out.println(
                    "\nAll pending tasks have been processed successfully!");

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();

            System.out.println(
                    "Task processing was interrupted. You can try again.");

        } catch (ExecutionException e) {
            executor.shutdownNow();

            System.out.println(
                    "Something went wrong while processing a task.");
        }
    }
}