package com.ssm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ssm.entity.JsonResult;
import com.ssm.entity.Submission;
import com.ssm.entity.User;
import com.ssm.service.INoPassService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Controller
public class NoPassController {
private INoPassService noPassService;

public INoPassService getNoPassService() {
	return noPassService;
}
@Resource
public void setNoPassService(INoPassService noPassService) {
	this.noPassService = noPassService;
}
@RequestMapping(value="/customer/restNopassJson", method = RequestMethod.GET)
public @ResponseBody JsonResult<Submission> showSubmission(HttpServletRequest req, int pageSize, int pageNum,
														   String sort, String order) {
	User user=(User) req.getSession().getAttribute("user");
	PageHelper.startPage(pageNum,pageSize,sort+" "+order);		
	PageInfo<Submission> page = new PageInfo<Submission>(noPassService.findAllNoPass(user.getUserid(),"待审核"));
	return new JsonResult<Submission>(page);
}
@RequestMapping(value = "/customer/{submissionid}/restNopassJson", method = RequestMethod.DELETE)
public @ResponseBody
JsonResult submissionidDelete(@PathVariable int submissionid) {
	boolean res = noPassService.deleteNoPass(submissionid);
	if (res) {
		return new JsonResult("删除成功");
	} else {
		return new JsonResult(JsonResult.ERROR, "删除失败！");
	}
}
@RequestMapping("/{userid}/getNopassPage")
public @ResponseBody JsonResult<Submission> getPage(@PathVariable String userid,@PathVariable String state){
	PageHelper.startPage(1,3);
	List<Submission> submission = noPassService.findAllNoPass(userid, state);
	PageInfo<Submission> page = new PageInfo<Submission>(submission);
	return new JsonResult<Submission>(page);
}
}
