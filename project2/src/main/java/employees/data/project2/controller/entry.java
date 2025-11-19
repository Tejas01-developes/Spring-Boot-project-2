package employees.data.project2.controller;

import employees.data.project2.email.email;
import employees.data.project2.refreshtoken.refreshtoken;
import employees.data.project2.repo.employeesrepo;
import employees.data.project2.schema.schema;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/info")
public class entry {
    @Autowired
    private employeesrepo employeesrepo;
    @Autowired
    private BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();
    @Autowired
    private email email;
    @Autowired
    private refreshtoken refreshtoken;

@PostMapping("/makeaccount")
    @CrossOrigin(origins = "http://localhost:5173" )
    public ResponseEntity<?>register(@RequestBody schema schema) throws MessagingException {
        Optional<schema>findusername=employeesrepo.findByusername(schema.getUsername());
        if(findusername.isPresent()){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(Map.of("success",false,"message","username is already reported"));
        }
Optional<schema>findemail=employeesrepo.findByemail(schema.getEmail());
        if(findemail.isPresent()){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(Map.of("success",false,"message","email is already reported"));
        }

        String hash=bcrypt.encode(schema.getPassword());
        schema.setPassword(hash);
        schema.setRole(List.of("Employees"));
        employeesrepo.save(schema);
        email.sendmails(
        schema.getEmail(),
        "welcome mail",
        "Welcome "+ schema.getUsername() + " your compny id is "+ schema.getId()
        );

return  ResponseEntity.ok(Map.of("success",true,"message","Account is succesfully created"));

    }

@PostMapping("/login")
@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
    public ResponseEntity<?>loginuser(@RequestBody schema schema){
    Optional<schema>findemail=employeesrepo.findByemail(schema.getEmail());
    if(findemail.isEmpty()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success",false,"message","email is not registered"));
    }
    schema data= findemail.get();
    if(!bcrypt.matches(schema.getPassword() , data.getPassword())){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success",false,"message","password is incorrect"));
    }
    String accesstoken= refreshtoken.accesstoken(data.getUsername(), String.join(",",data.getRole()));
    ResponseCookie cookie1=ResponseCookie.from("accesstokenn",accesstoken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(15 * 60)
            .build();

    String refreshtokens=refreshtoken.refreshtoken(data.getUsername(), String.join(",", data.getRole()));
    ResponseCookie cookie2=ResponseCookie.from("refreshtokenn",refreshtokens)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .build();

    return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE,cookie1.toString()).header(HttpHeaders.SET_COOKIE,cookie2.toString()).body(Map.of("success",true,"message","Login succesfull","role",data.getRole()));
}




}
