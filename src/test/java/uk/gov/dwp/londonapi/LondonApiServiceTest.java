package uk.gov.dwp.londonapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.londonapi.service.LondonApiService;
import uk.gov.dwp.londonapi.service.dto.User;
import uk.gov.dwp.londonapi.service.external.ExternalLondonApi;

@ExtendWith(MockitoExtension.class)
public class LondonApiServiceTest {

  @InjectMocks
  LondonApiService londonApiService;
  @Mock
  private ExternalLondonApi externalLondonApi;

  @Test
  @DisplayName("Given an empty list of user, it return an empty list of user in or near london")
  public void returnNoUser() {
    when(externalLondonApi.getUsers()).thenReturn(List.of());
    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    assertEquals(List.of(), usersInOrNearLondon,
        "should return the list of users in or near london");
  }

  @Test
  @DisplayName("Given city as london, then return all people in London")
  public void givenCityAsLondonReturnUsers() {
    when(externalLondonApi.getUserInCity("London")).thenReturn(getUsers("users_in_london.json"));

    List<User> users = londonApiService.getUserInCity("London");
    assertEquals(getUsers("users_in_london.json"), users,
        "should return the list of users in london");
  }

  @ParameterizedTest
  @CsvSource({"-7.5115909,130.652983, true", "-27.3482312,-51.6044276, false"})
  @DisplayName("Given a coordinate confirm if it's 50 miles of London.")
  public void checkIfCoordinateNearLondon(double latitude, double longitude, boolean expected) {

    boolean isNearLondon = londonApiService.isNearLondon(latitude, longitude,
        getUsers("users_in_london.json"));
    assertEquals(expected, isNearLondon, "should return true or false");
  }

  @Test
  @DisplayName("Given some users in london, ensure the list of users in or near London are returned"
      + " without duplicate")
  public void noduplicateUserReturned() {
    when(externalLondonApi.getUsers()).thenReturn(getAllUsers());
    when(externalLondonApi.getUserInCity("London")).thenReturn(getUsersInLondon());

    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    assertEquals(getUsersInOrNearLondonWithOutDuplicate(), usersInOrNearLondon,
        "should return the list of users without duplicates");
  }

  @Test
  @DisplayName("Given all users, return the users that are in london or 50 miles of London")
  public void returnUsersInLondonOrNear() {

    when(externalLondonApi.getUsers()).thenReturn(getUsers("all_users.json"));
    when(externalLondonApi.getUserInCity("London")).thenReturn(getUsers("users_in_london.json"));

    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    assertEquals(getUsers("users_in_or_near_london.json"), usersInOrNearLondon,
        "should return the list of users in or near london");
  }


  private List<User> getUsersInLondon() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk",
            -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk",
            -6.7098551, 111.3479498),
        new User("Stephen", "Mapstone", "smapstonei9@bandcamp.com",
            -8.1844859, 113.6680747)
    );
  }


  private List<User> getAllUsers() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk",
            -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk",
            -6.7098551, 111.3479498),
        new User("Stephen", "Mapstone", "smapstonei9@bandcamp.com",
            -8.1844859, 113.6680747),
        new User("Tripp", "Matzel", "tmatzelp@wikia.com",
            -27.3482312, -51.6044276),
        new User("Jeane", "de Juares", "jdejuaresi@exblog.jp",
            32.6797904, -5.5781378),
        new User("Hassan", "Liasu", "smapstonei9@bandcamp.com",
            -26.1844859, -52.6680747)
    );
  }

  private List<User> getUsersInOrNearLondonWithOutDuplicate() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk",
            -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk",
            -6.7098551, 111.3479498),
        new User("Stephen", "Mapstone", "smapstonei9@bandcamp.com",
            -8.1844859, 113.6680747)
    );
  }

  private List<User> getUsers(String name) {
    String userInLondon = getJsonString(name);
    try {
      return new ObjectMapper().readValue(userInLondon, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private String getJsonString(String name) {
    try {
      return new String(Files.readAllBytes(
          Paths.get(
              Objects.requireNonNull(getClass().getClassLoader().getResource(name)).toURI())));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
