package com.abocode.jfaster.web.system.interfaces.restful;
import com.abocode.jfaster.web.system.application.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserResource {
    @Autowired
    private IUserService userService;
    @PostMapping
    public Object list() throws Exception {
       return    userService.getAll();
    }
}
