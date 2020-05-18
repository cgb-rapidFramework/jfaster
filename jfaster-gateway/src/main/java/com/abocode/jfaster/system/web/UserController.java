package com.abocode.jfaster.system.web;

import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
@RestController
@RequestMapping("/users")
@Api(value = "用户信息", tags = "用户信息")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping()
    @ApiOperation(value = "获取所有用户信息", notes = "获取所有用户信息")
    public String  hello() {
        return  "hello";
    }

    @GetMapping("/findById")
    @ApiOperation(value = "根据id查询用户信息", notes = "根据id查询用户信息")
    public String  findById(String id) {
        Optional<User> ops = userRepository.findById(id);
        return  ops.get().getRealName();
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "获取所有用户信息", notes = "获取所有用户信息")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
