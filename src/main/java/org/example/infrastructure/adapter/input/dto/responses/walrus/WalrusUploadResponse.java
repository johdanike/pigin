package org.example.infrastructure.adapter.input.dto.responses.walrus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WalrusUploadResponse {
   private NewlyCreated newlyCreated;
   private AlreadyCertified alreadyCertified;

}
