package uk.gov.dwp.londonapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.londonapi.service.LondonApiService;
import uk.gov.dwp.londonapi.service.dto.User;
import uk.gov.dwp.londonapi.service.external.ExternalLondonApi;

@ExtendWith(MockitoExtension.class)
public class LondonApiServiceTest {

  @Mock
  private ExternalLondonApi externalLondonApi;

  @Test
  @DisplayName("Given city as london, then return all people in London")
  public void givenCityAsLondonReturnUsers() {
    when(externalLondonApi.getUserInCity("London")).thenReturn(getUsersInLondon());

    LondonApiService service = new LondonApiService(externalLondonApi);
    List<User> users = service.getUserInCity("London");
    assertEquals(getUsersInLondon(), users, "should return the list of users in london");
  }

  private List<User> getUsersInLondon() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk", -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk", -6.7098551, 111.3479498)
    );
  }

}