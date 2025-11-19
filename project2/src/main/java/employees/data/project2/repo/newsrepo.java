package employees.data.project2.repo;

import employees.data.project2.schema.newsschema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface newsrepo extends MongoRepository<newsschema,String> {
}
