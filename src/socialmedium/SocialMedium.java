package socialmedium;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class SocialMedium {
  MySqlConnection mySqlConnection;

  public SocialMedium() {
    mySqlConnection = new MySqlConnection();
  }

  public static void main(String[] args) {
    new SocialMedium().run();
  }

  private void run() {
    boolean running = true;

    while (running) {
      MenuChoice menuChoice = showMainMenu();
      switch (menuChoice) {
        case CREATE_PROFILE -> createProfile();
        case SHOW_ALL_PROFILES -> showAllProfiles();
        case QUIT -> running = false;
      }
    }
    mySqlConnection.closeConnection();
  }

  private void showAllProfiles() {
    ArrayList<Profile> profiles = mySqlConnection.getAllProfiles();
    printProfiles(profiles);
  }

  private void printProfiles(ArrayList<Profile> profiles) {
    for (Profile p : profiles) {
      System.out.println(p);
    }
  }

  private void createProfile() {
    Profile profile = userTypesProfile();
    mySqlConnection.addProfile(profile);
  }

  private Profile userTypesProfile() {
    Scanner in = new Scanner(System.in);
    System.out.println("\nCREATE PROFILE");
    System.out.print("First name: ");
    String firstName = in.nextLine();
    System.out.print("Last name: ");
    String lastName = in.nextLine();
    System.out.print("Email: ");
    String email = in.nextLine();
    System.out.print("Date of birth: ");
    String dateString = in.nextLine();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate dateOfBirth =LocalDate.parse(dateString, formatter);
    System.out.print("Gender (M/F/O): ");
    char genderChar = in.nextLine().toLowerCase().charAt(0);
    Profile profile = new Profile(firstName, lastName, email, dateOfBirth, genderCharToEnum(genderChar));
    return profile;
  }

  private Gender genderCharToEnum(char genderChar) {
    Gender gender = Gender.Other; // Default gender
    switch (genderChar) {
      case 'm' -> gender = Gender.Male;
      case 'f' -> gender = Gender.Female;
      case 'o' -> gender = Gender.Other;
    }
    return gender;
  }

  private MenuChoice showMainMenu() {
    Scanner in = new Scanner(System.in);

    System.out.println("\nMAIN MENU\n" +
        "1. Create profile\n" +
        "2. Show all profiles\n" +
        "Q. Quit\n");

    char choice = in.nextLine().toLowerCase().charAt(0);
    MenuChoice menuChoice = null;
    switch (choice) {
      case '1' -> menuChoice = MenuChoice.CREATE_PROFILE;
      case '2' -> menuChoice = MenuChoice.SHOW_ALL_PROFILES;
      case 'q' -> menuChoice = MenuChoice.QUIT;
    }
    return menuChoice;
  }
}
