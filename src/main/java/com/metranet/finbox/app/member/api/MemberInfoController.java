package com.metranet.finbox.app.member.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.metranet.finbox.lib.BaseController;
import com.metranet.finbox.lib.MemberDetail;
import com.metranet.finbox.lib.common.JsonConverterUtil;
import com.metranet.finbox.service.member.api.MemberService;
import com.metranet.finbox.service.member.dto.MemberDto;

@RestController
@RequestMapping("/api/microsite/member")
public class MemberInfoController extends BaseController {

    Logger logger = LoggerFactory.getLogger(MemberInfoController.class);
    
    @Autowired
    MemberService memberService;
    
    @GetMapping("/info")
    public ResponseEntity<String> info(HttpServletRequest request) {
        
        // Get Member Detail from Session
        MemberDetail memberDetail = getMemberDetail(request);
        if(null == memberDetail) {
            return new ResponseEntity<String>(
                    "{\"msg\":\"Invalid credential\"}", 
                    HttpStatus.BAD_REQUEST);
        }
        
        // Gather User Detail based on Member Detail Username
        MemberDto memberDto = memberService.findByUsername(memberDetail.getUserName());
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(memberDto), 
                HttpStatus.OK);
    }
    
    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<String> update(HttpServletRequest request, @RequestBody MemberDto member) {
        // Get Member Detail from Session
        MemberDetail memberDetail = getMemberDetail(request);
        if(null == memberDetail) {
            return new ResponseEntity<String>(
                    "{\"msg\":\"Invalid credential\"}", 
                    HttpStatus.BAD_REQUEST);
        }
        
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
