package demo.usul.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Collection;

@Node
@Getter
@Setter
public class Actress {

    @Id
    private String name;

    private Collection<String> alias;
}
