package pg.ium.warehouse.security.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthTokenVerificationRequest implements Serializable {
	private String idToken;
}
