package uk.gov.dwp.londonapi.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import uk.gov.dwp.londonapi.service.dto.User;

public class LondonApiHelper {

  public List<User> getUsersInLondon() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk",
            -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk",
            -6.7098551, 111.3479498),
        new User("Stephen", "Mapstone", "smapstonei9@bandcamp.com",
            -8.1844859, 113.6680747)
    );
  }

  public List<User> getAllUsers() {
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

  public List<User> getUsersInOrNearLondonWithOutDuplicate() {
    return List.of(
        new User("Mechelle", "Boam", "mboam3q@thetimes.co.uk",
            -6.5115909, 105.652983),
        new User("Terry", "Stowgill", "tstowgillaz@webeden.co.uk",
            -6.7098551, 111.3479498),
        new User("Stephen", "Mapstone", "smapstonei9@bandcamp.com",
            -8.1844859, 113.6680747)
    );
  }

  public List<User> getUsers(String name) {
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
