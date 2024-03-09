package demo.usul.domain;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class ActIn {

    @TargetNode
    private Video video;
}
