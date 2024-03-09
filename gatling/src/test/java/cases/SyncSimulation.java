package cases;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SyncSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://127.0.0.1:8080");

    ScenarioBuilder scn =
            scenario("sync test")
                    .exec(
                            http("sync test")
                                    .get("/stress/sync")
                                    .check(status().is(200))
                    );

    {
        setUp(scn.injectOpen(
                atOnceUsers(1000),
                constantUsersPerSec(200).during(15)
        ).protocols(httpProtocol));
    }
}
