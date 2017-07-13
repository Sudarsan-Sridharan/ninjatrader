package com.bn.ninjatrader.auth.token;

/**
 * @author bradwee2000@gmail.com
 */
public interface TokenVerifier {

    DecodedToken verifyToken(final String token);
}
