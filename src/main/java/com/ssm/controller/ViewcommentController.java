package com.ssm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.entity.JsonResult;
import com.ssm.entity.StanVideoComm;
import com.ssm.entity.User;
import com.ssm.entity.Videocomment;
import com.ssm.service.IVideocommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@RestController
public class ViewcommentController {
private IVideocommentService videocommentService;

public IVideocommentService getVideocommentService() {
	return videocommentService;
}
@Resource
public void setVideocommentService(IVideocommentService videocommentService) {
	this.videocommentService = videocommentService;
}
@RequestMapping(value="/customer/restCommentJson", method = RequestMethod.GET)
public  Object showVideocomment(int pageSize, int pageNum, String sort, String order, HttpServletRequest req) throws JsonProcessingException {
	int pn=pageNum/10+1;
	User user=(User)req.getSession().getAttribute("user");
	PageHelper.startPage(pn,pageSize,sort+" "+order);		
	PageInfo<StanVideoComm> page = new PageInfo<StanVideoComm>(videocommentService.uniteVideoComm(user.getUserid()));
	ObjectMapper objectMapper=new ObjectMapper();
	String json=objectMapper.writeValueAsString(new JsonResult<StanVideoComm>(page));
	return json;
}
@RequestMapping(value = "/customer/{commentid}/restCommentJson", method = RequestMethod.DELETE)
public  Object useridDelete(@PathVariable int commentid) throws JsonProcessingException {
	boolean res = videocommentService.deleteVideocomment(commentid);
	ObjectMapper objectMapper=new ObjectMapper();
	if (res) {
		String json=objectMapper.writeValueAsString(new JsonResult("删除成功"));
		return json;
	} else {
		String json=objectMapper.writeValueAsString(new JsonResult(JsonResult.ERROR, "删除失败！"));
		return json;
	}
}
@RequestMapping("/{userid}/getCommentPage")
public  Object getPage(@PathVariable String userid) throws JsonProcessingException {
	PageHelper.startPage(1,3);
	List<Videocomment> videocomment = videocommentService.findAllVideocomment(userid);
	PageInfo<Videocomment> page = new PageInfo<Videocomment>(videocomment);
	ObjectMapper objectMapper=new ObjectMapper();
	String json=objectMapper.writeValueAsString(new JsonResult<Videocomment>(page));
	return json;
}
}

