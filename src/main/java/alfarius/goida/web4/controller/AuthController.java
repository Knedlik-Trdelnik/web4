package alfarius.goida.web4.controller;


import alfarius.goida.web4.exceptions.NoSuchLoginException;
import alfarius.goida.web4.models.LoginRequest;
import alfarius.goida.web4.repository.UserRepository;
import io.helidon.security.jwt.Jwt;
import io.helidon.security.jwt.SignedJwt;
import io.helidon.security.jwt.jwk.JwkEC;
import io.helidon.security.jwt.jwk.JwkRSA;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import alfarius.goida.web4.models.User;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

//ssh -L 5432:localhost:5432 s467969@helios.cs.ifmo.ru -p 2222
@Path("/auth")
@ApplicationScoped
@PermitAll
public class AuthController {
    @Inject
    UserRepository userRepository;
    @Inject
    private Config config;

    @POST()
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(LoginRequest loginRequest) {
        String name = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println(name);
        System.out.println(password);
        User user = null;
        try {
            user = userRepository.findUserByLogin(name);
            if(password.equals(user.getPassword())) {
                String token = createJWTToken(user);
                String jsonResponse = String.format("{\"token\": \"%s\", \"tokenType\": \"Bearer\"}", token);
                return Response.ok(jsonResponse).build();
            }

        } catch (NoSuchLoginException e) {
            System.out.println("У него нет имени хахахахха");
            return Response.status(418).build();
        }
        System.out.println("Он не смог залогинитья...");
        return Response.status(418).build();
    }


    private String createJWTToken(User user) {
        try (InputStream is = getClass().getResourceAsStream("/jwt-keys/publicKey.pem")) {
            if (is == null) {
                System.err.println("❌❌❌ Файл публичного ключа НЕ НАЙДЕН в classpath!");
            } else {
                System.out.println("✅✅✅ Файл публичного ключа НАЙДЕН. Размер: " + is.readAllBytes().length + " байт.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Instant now = Instant.now();
            Instant expires = now.plus(Duration.ofHours(1));
            String username = user.getLogin();

            Jwt jwt = Jwt.builder()
                    .subject(username)
                    .issuer("goida-lab-4")
                    .issueTime(now)
                    .expirationTime(expires)
                    .addPayloadClaim("upn", username)
                    .build();

            // Загружаем приватный ключ из файла
            try (InputStream is = getClass().getResourceAsStream("/jwt-keys/privateKeyPkcs8.pem")) {
                if (is == null) {
                    throw new IllegalStateException("Private key not found: /jwt-keys/privateKeyPkcs8.pem");
                }
                String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s+", "");

                byte[] keyBytes = Base64.getDecoder().decode(pem);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

                KeyFactory kf = KeyFactory.getInstance("RSA");
                RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);

                JwkRSA jwk = JwkRSA.builder()
                        .privateKey(privateKey)
                        .algorithm("RS256")
                        .keyId("local-key-1")
                        .build();

                SignedJwt signed = SignedJwt.sign(jwt, jwk);
                return signed.tokenContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }



}
