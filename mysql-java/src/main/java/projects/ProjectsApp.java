/**
 * 
 */
package projects;

import java.math.BigDecimal;
import java.util.*;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {

	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();

	// List of selections for user
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project"
			);
	// @formatter:on

	public static void main(String[] args) {

		// Creates the application object and calls the method that will process user
		// selections
		new ProjectsApp().processUserSelections();

	}

	// This method will call methods to display the menu, get user selection, and
	// act on that selection
	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;

				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
					
				case 1:
					createProject();
					break;
				}

			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}

	}
	
	// This method creates a new project object, details it with user input, and saves it to another object
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficuly (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
		
	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	// This method prints an exit message and returns true to end the while loop
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	// Calls print menu method and takes input from user - if no input is given,
	// input is null, a -1 is returned
	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;

	}

	// This method calls the method that will get user input and determines if it is
	// null or an integer
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	// This method looks for user input
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": "); // Print (not println) Keeps the cursor on the same line as the prompt
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}

	// Prints the menu
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		operations.forEach(line -> System.out.println("     " + line));
	}

}
