package com.commerce.web.member.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.lib.JsonConverterUtil;
import com.commerce.lib.SecurityUtil;
import com.commerce.web.member.config.annotation.NeedLogin;
import com.commerce.web.member.dto.MemberDetail;
import com.commerce.web.member.dto.MemberDto;
import com.commerce.web.member.service.MemberService;

@RestController
@RequestMapping("/api/microsite/member")
public class MemberController {

    Logger logger = LoggerFactory.getLogger(MemberController.class);
    
    @Autowired
    MemberService memberService;
    
    @PostMapping(value = "/pub/register", consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody MemberDto member) {
        
        // Encrypt password and Save Data,
        MemberDto param = new MemberDto();
        param.setUsername(member.getUsername());
        param.setEmail(member.getEmail());
        param.setPhone(member.getPhone());
        param.setStatus("ACTIVE");
        param.setType("BUYER");
        param.setImage(member.getImage());
        param.setPassword(SecurityUtil.encrypt(member.getPassword()));
        
        Long id = memberService.registerMember(param);
        if(id > 0) {
            param.setId(id);
        }
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(param), 
                HttpStatus.OK);
    }
    
    @NeedLogin
    @GetMapping("/pvt/info")
    public ResponseEntity<String> info(MemberDetail memberDetail) {
        
        // Gather User Detail based on Member Detail Username
        MemberDto memberDto = memberService.findByUsername(memberDetail.getUserName());
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(memberDto), 
                HttpStatus.OK);
    }
    
    @NeedLogin
    @PostMapping(value = "/pvt/update", consumes = "application/json")
    public ResponseEntity<String> update(MemberDetail memberDetail, @RequestBody MemberDto member) {
        
        // Gather User Detail based on Member Detail Username
        MemberDto memberDto = memberService.findByUsername(memberDetail.getUserName());
        
        // Prepare and Update Parameter
        member.setId(memberDto.getId());
        
        if(memberService.updateMember(member) > 0) {
        	// Show the response
        	return new ResponseEntity<String>(
                    "{\"msg\":\"Success Update Member\"}", 
                    HttpStatus.OK);
        } else {
        	// Show the response
        	return new ResponseEntity<String>(
                    "{\"msg\":\"Failed Update Member\"}", 
                    HttpStatus.OK);
        }
    }
}
