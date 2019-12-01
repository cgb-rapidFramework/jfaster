package com.abocode.jfaster.web.system.interfaces.restful;
import com.abocode.jfaster.web.system.application.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users")
public class UserResource {
    @Autowired
    private IUserService userService;
    @GetMapping
    public Object list(){
       return    userService.getAll();
    }
}
