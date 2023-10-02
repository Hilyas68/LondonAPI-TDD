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
  @DisplayName("Given city as london, then return all people in London")
  public void givenCityAsLondonReturnUsers() {
    when(externalLondonApi.getUserInCity("London")).thenReturn(getUsers("users_in_london.json"));

    List<User> users = londonApiService.getUserInCity("London");
    assertEquals(getUsers("users_in_london.json"), users,
        "should return the list of users in london");
  }

  @Test
  @DisplayName("Given a coordinate confirm if it's 50 miles of London.")
  public void checkIfCoordinateNearLondon() {

    boolean isNearLondon = londonApiService.isNearLondon(-7.5115909, 130.652983,
        getUsers("users_in_london.json"));
    assertTrue(isNearLondon, "should return true or false");
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

  @Test
  @DisplayName("Given an empty list of user, it return an empty list of user in near london")
  public void returnNoUser() {
    when(externalLondonApi.getUsers()).thenReturn(List.of());
    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    assertEquals(List.of(), usersInOrNearLondon,
        "should return the list of users in or near london");
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
