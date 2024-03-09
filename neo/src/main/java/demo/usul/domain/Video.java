package demo.usul.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
public class Video {

    @Id
    private final String name;

    private final String code;

    private final String num;

    private final String title;

}
