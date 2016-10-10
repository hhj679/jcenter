package com.hw.bbs.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hw.bbs.entity.User;

@RestController
@RequestMapping(value="/users")
public class UserController {
	
//	@Autowired
//	private UserMapper userMapper;

    @RequestMapping(value="/{uid}", method=RequestMethod.GET)
    public User index(@PathVariable Long uid) {
		return null;
//        return userMapper.findByUserUid(uid);
    }

}