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
}
