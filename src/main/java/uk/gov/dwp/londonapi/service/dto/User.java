package uk.gov.dwp.londonapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  private String firstName;
  private String lastName;
  private String email;
  private double latitude;
  private double longitude;

}
