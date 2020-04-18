package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.garrettestrin.PrivateGram.data.DataObjects.Invite;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvitesResponse {
  public List<Invite> invites;
  public boolean success;
}
