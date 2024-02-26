package socialmedium;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MySqlConnection {
  private String database = "jdbc:mysql://localhost:3306/social_medium";
  private String username = "social_medium_user";
  private String password = "social4ever";
  private Connection connection = null;

  public MySqlConnection() {
    createConnection();
  }

  private void createConnection() {
    if (connection != null)
      return; // If connection already created, just return that to ensure singleton

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(database, username, password);
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }
  }

  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      System.out.println("EXCEPTION: " + e.getStackTrace());
    }
    connection = null;
  }

  public void addProfile(Profile profile) {
    try {
      String query = "INSERT INTO profile (firstname, lastname, email, date_of_birth, gender) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, profile.getFirstName());
      preparedStatement.setString(2, profile.getLastName());
      preparedStatement.setString(3, profile.getEmail());
      preparedStatement.setDate(4, Date.valueOf(profile.getDateOfBirth()));
      preparedStatement.setString(5, profile.getGender().toString());

      preparedStatement.executeUpdate();

      System.out.println("Profile added successfully.");
    } catch (SQLException e) {
      System.out.println("EXCEPTION: " + e.getStackTrace());
    }
  }

  public ArrayList<Profile> getAllProfiles() {
    ArrayList<Profile> profiles = new ArrayList<>();
    String query = "SELECT * FROM profile;";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
      while (rs.next()) {
        int profileId = rs.getInt("profile_id");
        String firstName = rs.getString("firstname");
        String lastName = rs.getString("lastname");
        String email = rs.getString("email");
        LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
        Gender gender = Gender.valueOf(rs.getString("gender"));
        Profile profile = new Profile(profileId, firstName, lastName, email, dateOfBirth, gender);
        profiles.add(profile);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return profiles;
  }
}
