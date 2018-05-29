package com.abocode.jfaster.web.system.resources;
import com.abocode.jfaster.web.system.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserResource {
    @Autowired
    private IUserService userService;
    @GetMapping
    public Object list() throws Exception {
       return    userService.getAll();
    }
}
