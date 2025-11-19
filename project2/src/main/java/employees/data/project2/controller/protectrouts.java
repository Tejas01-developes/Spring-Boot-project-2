package employees.data.project2.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/protect")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class protectrouts {
    @GetMapping("/routes")
    public ResponseEntity<?>routes(HttpServletRequest req){
        String username=(String) req.getAttribute("username");
        if(username==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success",false,"message","username is null"));
        }
        return ResponseEntity.ok(Map.of("success",true,"username",username));


    }

}
