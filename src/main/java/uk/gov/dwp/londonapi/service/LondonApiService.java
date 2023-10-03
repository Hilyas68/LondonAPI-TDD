package uk.gov.dwp.londonapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    if (!userInLondon.isEmpty()) {
      for (User user : userInLondon) {
        if (calculateDistance(latitude, longitude, user.getLatitude(), user.getLongitude())
            <= MAX_DISTANCE) {
          return true;
        }
      }
    } else {
      double londonLatitude = 51.5072;
      double londonLongitude = 0.1275;

      return calculateDistance(latitude, longitude, londonLatitude, londonLongitude)
          <= MAX_DISTANCE;
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

  private double calculateDistance(double latitude, double longitude, double londonLatitude,
      double londonLongitude) {
    return Math.sqrt(
        Math.pow(latitude - londonLatitude, 2) + Math.pow(
            longitude - londonLongitude, 2));
  }
}
