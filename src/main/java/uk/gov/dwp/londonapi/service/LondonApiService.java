package uk.gov.dwp.londonapi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.dwp.londonapi.service.dto.User;
import uk.gov.dwp.londonapi.service.external.ExternalLondonApi;

@Service
@RequiredArgsConstructor
public class LondonApiService {

  private final ExternalLondonApi externalLondonApi;

  public List<User> getUserInCity(String city) {
    return externalLondonApi.getUserInCity(city);
  }

  public boolean isNearLondon(double latitude, double longitude, List<User> userInLondon) {

    for (User user : userInLondon) {
      double calculatedDistance = Math.sqrt(
          Math.pow(latitude - user.getLatitude(), 2) + Math.pow(longitude - user.getLongitude(), 2));

      if (calculatedDistance <= 50) {
        return true;
      }
    }
    return false;
  }
}
