package alfarius.goida.web4.trash;

import io.helidon.microprofile.jwt.auth.JwtAuthProvider;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@LoginConfig(authMethod = "MP-JWT")
public class SecuredApplication extends Application {

}
