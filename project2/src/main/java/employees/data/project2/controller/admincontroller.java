package employees.data.project2.controller;

import employees.data.project2.refreshtoken.refreshtoken;
import employees.data.project2.repo.employeesrepo;
import employees.data.project2.repo.newsrepo;
import employees.data.project2.schema.newsschema;
import employees.data.project2.schema.schema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class admincontroller {
    @Autowired
    private employeesrepo employeesrepo;
    @Autowired
    private refreshtoken refreshtoken;
    @Autowired
    private newsrepo newsrepo;
    @Autowired
    private BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();
    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    @CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
    public ResponseEntity<?>addnews(@RequestBody newsschema newsschema,HttpServletRequest req){
        String role=(String)req.getAttribute("role");
        if(!"Admin".equalsIgnoreCase(role)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Access denied"));
        }
         newsrepo.save(newsschema);
         return ResponseEntity.ok(Map.of("success",true,"message","News added"));
    }

    @GetMapping("/get")
    @CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
    public ResponseEntity<List<newsschema>>getallnews(){
        List<newsschema>newss=newsrepo.findAll();
        return ResponseEntity.ok(newss);
    }

@GetMapping("/getall")
@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
@PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?>getallemployees(HttpServletRequest req){
       String role=(String) req.getAttribute("role");
       if(!"Admin".equalsIgnoreCase(role)){
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Access denied"));
       }
           List<schema> users=employeesrepo.findAll();
       return ResponseEntity.ok(Map.of("success",true,"users",users));

}

@PostMapping("/addadmin")
@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
@PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?>addadmin(@RequestBody schema schema,HttpServletRequest req){
        String role=(String) req.getAttribute("role");
        if(!"Admin".equalsIgnoreCase(role)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success",false,"message","you are not Admin"));
        }
        schema.setRole(List.of("Admin"));
        String hash= bcrypt.encode(schema.getPassword());
        schema.setPassword(hash);
        employeesrepo.save(schema);
        return ResponseEntity.ok(Map.of("success",true,"message","Admin added succesfully"));

}





}
