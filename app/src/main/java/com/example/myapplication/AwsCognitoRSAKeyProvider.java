package com.example.myapplication;


import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class AwsCognitoRSAKeyProvider implements RSAKeyProvider {

    private final URL aws_kid_store_url;
    private final JwkProvider provider;

    public AwsCognitoRSAKeyProvider(String aws_cognito_region, String aws_user_pools_id) {
        String url = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", aws_cognito_region, aws_user_pools_id);
        try {
            aws_kid_store_url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Invalid URL provided, URL=%s", url));
        }
        provider = new JwkProviderBuilder(aws_kid_store_url).build();
    }

    public void getter(String token){

        String aws_cognito_region = "us-east-2"; // Replace this with your aws cognito region
        String aws_user_pools_id = "us-east-2_si1cYK2IO"; // Replace this with your aws user pools id
        RSAKeyProvider keyProvider = new AwsCognitoRSAKeyProvider(aws_cognito_region, aws_user_pools_id);
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                //.withAudience("2qm9sgg2kh21masuas88vjc9se") // Validate your apps audience if needed
                .build();

        //String token = "eyJraWQiOiJjdE.eyJzdWIiOiI5NTMxN2E.VX819z1A1rJij2"; // Replace this with your JWT token
        DecodedJWT jwt= jwtVerifier.verify(token);

    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        try {
            return (RSAPublicKey) provider.get(kid).getPublicKey();
        } catch (JwkException e) {
            throw new RuntimeException(String.format("Failed to get JWT kid=%s from aws_kid_store_url=%s", kid, aws_kid_store_url));
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}