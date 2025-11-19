package employees.data.project2.repo;

import employees.data.project2.schema.schema;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface employeesrepo extends MongoRepository<schema,String> {
    Optional<schema>findByemail(String email);
    Optional<schema>findByusername(String username);

}
