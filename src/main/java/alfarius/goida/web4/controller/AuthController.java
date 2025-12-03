package alfarius.goida.web4.controller;


import alfarius.goida.web4.exceptions.NameIsAlreadyExistException;
import alfarius.goida.web4.exceptions.NoSuchLoginException;
import alfarius.goida.web4.models.LoginRequest;
import alfarius.goida.web4.repository.UserRepository;
import io.helidon.security.jwt.Jwt;
import io.helidon.security.jwt.SignedJwt;
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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

//ssh -L 5432:localhost:5432 s467969@helios.cs.ifmo.ru -p 2222
@Path("/auth")
@ApplicationScoped
@PermitAll
public class AuthController {
    @Inject
    UserRepository userRepository;

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
        String hashPass = "";
        try {
            hashPass = hashPassword(password, name);
        } catch (NoSuchAlgorithmException e) {
            return Response.status(500).build();
        }

        try {
            user = userRepository.findUserByLogin(name);
            if (hashPass.equals(user.getPassword())) {
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


    @POST()
    @Path("/register")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register(LoginRequest loginRequest) {
        String name = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        System.out.println("У нас очередь на регистрацию! (Данные ниже) ");
        System.out.println(name);
        System.out.println(password);
        User user = new User();
        user.setLogin(name);
        try {
            user.setPassword(hashPassword((password), name));
        } catch (NoSuchAlgorithmException e) {
            return Response.status(500).build();
        }

        try {
            userRepository.addUser(user);
            String response = "Успешная авторизация!";
            return Response.ok(response).build();

        } catch (NameIsAlreadyExistException e) {
            System.out.println(e.getMessage());
            System.out.println("Имя повторилось(");
            return Response.status(418).build();
        }
    }

    private String hashPassword(String password, String name) throws NoSuchAlgorithmException {
        String salt = String.valueOf(name.hashCode());
        String finalPass = password + salt;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(finalPass.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
