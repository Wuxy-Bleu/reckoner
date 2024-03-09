package cases;


import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.extern.slf4j.Slf4j;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.bodyString;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

@Slf4j
public class SnowflakeSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://127.0.0.1:9149");

    ScenarioBuilder scn =
            scenario("call_for_snowflake_ids")
                    .exec(
                            http("call_for_snowflake_ids")
                                    .get("/v1/snowflake")
                                    .check(status().is(200))
                                    .check(bodyString().saveAs("ids"))
                    )
                    .exec(session -> {
                        log.info(session.get("ids"));
                        return session;
                    });

    {
        setUp(scn.injectOpen(
                atOnceUsers(10), // 2
                constantUsersPerSec(200).during(15) // 4
        ).protocols(httpProtocol));
    }
}
