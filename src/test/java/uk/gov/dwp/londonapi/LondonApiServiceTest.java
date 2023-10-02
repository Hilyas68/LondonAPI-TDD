package uk.gov.dwp.londonapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    assertEquals(getUsers("users_in_london.json"), users, "should return the list of users in london");
  }

  @Test
  @DisplayName("Given a coordinate confirm if it's 50 miles of London.")
  public void checkIfCoordinateNearLondon() {

    boolean isNearLondon = londonApiService.isNearLondon(-7.5115909, 130.652983,
        getUsers("users_in_london.json"));
    assertEquals(true, isNearLondon, "should return true or false");
  }

  private List<User> getUsersInOrNearLondon() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk", -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk", -6.7098551, 111.3479498)
    );
  }

  private List<User> getUsers(String name) {
    String userInLondon = getJsonString(name);
    try {
      return new ObjectMapper().readValue(userInLondon, new TypeReference<>() {});
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
