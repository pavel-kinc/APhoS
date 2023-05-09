package cz.muni.aphos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Configuration for swagger-ui interface for API with annotations and descriptions.
 */
@OpenAPIDefinition(
        info = @Info(title = "APhoS",
                version = "2.0.1",
                description = """
                        This is Amateur Photometric Survey (APhoS) Application Programming Interface.
                        """,
                contact = @Contact(name = "Pavel Kinc", email = "pavelkinc230@gmail.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = {@Server(description = "Main server APhoS", url = "https://aphos.cerit-sc.cz/"),
//                @Server(description = "Developer server", url = "http://localhost:8009"),
//                @Server(description = "Test server (virtual machine)", url = "https://ip-147-251-21-104.flt.cloud.muni.cz/")
        },
        tags = {@Tag(name = "SpaceObject", description = "Everything about space objects"),
                @Tag(name = "Flux", description = "Data about a flux"),
                @Tag(name = "User", description = "User information")}
)
public class SwaggerConfig {

}