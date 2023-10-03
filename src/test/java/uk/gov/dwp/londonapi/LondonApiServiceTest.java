package uk.gov.dwp.londonapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.londonapi.helper.LondonApiHelper;
import uk.gov.dwp.londonapi.service.LondonApiService;
import uk.gov.dwp.londonapi.service.dto.User;
import uk.gov.dwp.londonapi.service.external.ExternalLondonApi;

@ExtendWith(MockitoExtension.class)
public class LondonApiServiceTest {

  @InjectMocks
  LondonApiService londonApiService;
  @Mock
  private ExternalLondonApi externalLondonApi;
  private LondonApiHelper helper;

  @BeforeEach
  public void setup() {
    helper = new LondonApiHelper();
  }

  @Test
  @DisplayName(
      "Given an empty list of user in london, it return an empty list of user in or near london"
          + "and get all users in not called")
  public void returnNoUser() {
    when(externalLondonApi.getUserInCity("London")).thenReturn(List.of());
    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    verify(externalLondonApi, times(0)).getUsers();
    assertEquals(List.of(), usersInOrNearLondon,
        "should return the list of users in or near london");
  }

  @Test
  @DisplayName("Given city as london, then return all people in London")
  public void givenCityAsLondonReturnUsers() {
    when(externalLondonApi.getUserInCity("London")).thenReturn(
        helper.getUsers("users_in_london.json"));

    List<User> users = londonApiService.getUserInCity("London");
    verify(externalLondonApi, times(1)).getUserInCity("London");
    assertEquals(helper.getUsers("users_in_london.json"), users,
        "should return the list of users in london");
  }

  @ParameterizedTest
  @CsvSource({"-7.5115909,130.652983, true", "-27.3482312,-51.6044276, false"})
  @DisplayName("Given a coordinate confirm if it's 50 miles of London.")
  public void checkIfCoordinateNearLondon(double latitude, double longitude, boolean expected) {

    boolean isNearLondon = londonApiService.isNearLondon(latitude, longitude,
        helper.getUsers("users_in_london.json"));
    assertEquals(expected, isNearLondon, "should return true or false");
  }

  @Test
  @DisplayName("Given some users in london, ensure the list of users in or near London are returned"
      + " without duplicate")
  public void noduplicateUserReturned() {
    when(externalLondonApi.getUsers()).thenReturn(helper.getAllUsers());
    when(externalLondonApi.getUserInCity("London")).thenReturn(helper.getUsersInLondon());

    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    assertEquals(helper.getUsersInOrNearLondonWithOutDuplicate(), usersInOrNearLondon,
        "should return the list of users without duplicates");
  }

  @Test
  @DisplayName("Given all users, return the users that are in london or 50 miles of London")
  public void returnUsersInLondonOrNear() {

    when(externalLondonApi.getUsers()).thenReturn(helper.getUsers("all_users.json"));
    when(externalLondonApi.getUserInCity("London")).thenReturn(
        helper.getUsers("users_in_london.json"));

    List<User> usersInOrNearLondon = londonApiService.geUsersInOrNearLondon();
    assertEquals(helper.getUsers("users_in_or_near_london.json"), usersInOrNearLondon,
        "should return the list of users in or near london");
  }

}
