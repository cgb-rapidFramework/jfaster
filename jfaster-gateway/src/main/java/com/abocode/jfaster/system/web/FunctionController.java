package com.abocode.jfaster.system.web;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.repository.FunctionRepository;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 菜单权限处理类
 * 
 * @author 张代浩
 * 
 */
@RestController
@RequestMapping("/功能菜单")
@Api(value = "菜单信息", tags = "菜单信息")
public class FunctionController {
    @Autowired
    private FunctionRepository functionRepository;
    @GetMapping("/findAll")
    @ApiOperation(value = "获取所有菜单信息", notes = "获取所有菜单信息")
    public List<Function> findAll() {
        return functionRepository.findAll();
    }

}
