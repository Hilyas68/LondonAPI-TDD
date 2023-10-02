package uk.gov.dwp.londonapi.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.dwp.londonapi.service.dto.User;
import uk.gov.dwp.londonapi.service.external.ExternalLondonApi;

@Service
@RequiredArgsConstructor
public class LondonApiService {

  public static final int MAX_DISTANCE = 50;
  private final ExternalLondonApi externalLondonApi;

  public List<User> getUserInCity(String city) {
    return externalLondonApi.getUserInCity(city);
  }

  public boolean isNearLondon(double latitude, double longitude, List<User> userInLondon) {

    for (User user : userInLondon) {
      if (calculateDistance(latitude, longitude, user) <= MAX_DISTANCE) {
        return true;
      }
    }
    return false;
  }

  public List<User> geUsersInOrNearLondon() {

    final List<User> userInOrNearLondon = new ArrayList<>();
    final List<User> userInLondon = externalLondonApi.getUserInCity("London");
    final List<User> users = externalLondonApi.getUsers();
    for (User user : users) {
      if (isNearLondon(user.getLatitude(), user.getLongitude(), userInLondon)) {
        userInOrNearLondon.add(user);
      }
    }

    return userInOrNearLondon;
  }

  private double calculateDistance(double latitude, double longitude, User user) {
    return Math.sqrt(
        Math.pow(latitude - user.getLatitude(), 2) + Math.pow(longitude - user.getLongitude(), 2));
  }

}
