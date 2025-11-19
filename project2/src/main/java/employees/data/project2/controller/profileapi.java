package employees.data.project2.controller;

import employees.data.project2.repo.employeesrepo;
import employees.data.project2.schema.schema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class profileapi {
    @Autowired
    private employeesrepo employeesrepo;
    @GetMapping("/data")
    @CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
    public ResponseEntity<?>getprofiledata(HttpServletRequest req) {
        String username = (String) req.getAttribute("username");
        Optional<schema>findusername=employeesrepo.findByusername(username);
        if (findusername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Login again"));
        }
        schema data= findusername.get();

        Map<String,Object> profile=Map.of(
                "id",data.getId(),
                "username",data.getUsername(),
                "email",data.getEmail(),
                "role",data.getRole(),
                "sector",data.getDepartments()
        );

        return ResponseEntity.ok(Map.of("success",true,"profiledata",profile));

    }
}
