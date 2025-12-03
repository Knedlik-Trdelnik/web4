package alfarius.goida.web4.trash;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationScoped
@LoginConfig(authMethod = "MP-JWT")
public class SecuredApplication extends Application {

}
