package studenttaskmanager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TaskManager manager = new TaskManager();

        while (true) {

            System.out.println(
                    "\n========== STUDENT TASK MANAGER ==========");
            System.out.println("1. Add a New Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Search for a Task");
            System.out.println("4. Mark Task as Completed");
            System.out.println("5. Delete a Task");
            System.out.println("6. Process Pending Tasks");
            System.out.println("7. View Completed Tasks");
            System.out.println("8. View Pending Tasks");
            System.out.println("9. Exit");

            System.out.print("Choose an option (1-9): ");

            try {
                int choice = Integer.parseInt(
                        sc.nextLine().trim());

                switch (choice) {

                    case 1: {
                        System.out.print(
                                "Enter a task description: ");

                        String name = sc.nextLine();
                        manager.addTask(name);
                        break;
                    }

                    case 2: {
                        manager.viewTasks();
                        break;
                    }

                    case 3: {
                        System.out.print(
                                "Enter the task ID: ");

                        int id = Integer.parseInt(
                                sc.nextLine().trim());

                        manager.searchTask(id);
                        break;
                    }

                    case 4: {
                        System.out.print(
                                "Enter the task ID to complete: ");

                        int id = Integer.parseInt(
                                sc.nextLine().trim());

                        manager.completeTask(id);
                        break;
                    }

                    case 5: {
                        System.out.print(
                                "Enter the task ID to delete: ");

                        int id = Integer.parseInt(
                                sc.nextLine().trim());

                        manager.deleteTask(id);
                        break;
                    }

                    case 6: {
                        manager.processTasks();
                        break;
                    }

                    case 7: {
                        manager.viewByStatus(true);
                        break;
                    }

                    case 8: {
                        manager.viewByStatus(false);
                        break;
                    }

                    case 9: {
                        System.out.println(
                                "\nThank you for using Student Task Manager!");
                        System.out.println(
                                "Have a productive day. Goodbye!");

                        sc.close();
                        return;
                    }

                    default: {
                        System.out.println(
                                "\nThat option isn't available. "
                                + "Please choose a number from 1 to 9.");
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "\nInvalid input. Please enter a valid number.");

            } catch (TaskException e) {
                System.out.println(
                        "\n" + e.getMessage());
            }
        }
    }
}