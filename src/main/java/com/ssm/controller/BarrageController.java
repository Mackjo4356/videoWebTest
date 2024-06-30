package com.ssm.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.entity.Barrage;
import com.ssm.entity.JsonResult;
import com.ssm.entity.User;
import com.ssm.service.IBarrageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@RestController
public class BarrageController {
private IBarrageService barrageService;

public IBarrageService getBarrageService() {
	return barrageService;
}
@Resource
public void setBarrageService(IBarrageService barrageService) {
	this.barrageService = barrageService;
}
@RequestMapping(value="/customer/restBarrageJson", method = RequestMethod.GET)
public  Object showBarrage(int pageSize, int pageNum, String sort, String order, HttpServletRequest req) throws JsonProcessingException {
	int pn=pageNum/10+1;
	User user=(User) req.getSession().getAttribute("user");
	PageHelper.startPage(pn,pageSize,sort+" "+order);		
	PageInfo<Barrage> page = new PageInfo<Barrage>(barrageService.uniteBarrage(user.getUserid()));
	ObjectMapper objectMapper=new ObjectMapper();
	String json=objectMapper.writeValueAsString(new JsonResult<Barrage>(page));
	return json;
}
@RequestMapping(value = "/customer/{barrageid}/restBarrageJson", method = RequestMethod.DELETE)
public Object barrageidDelete(@PathVariable int barrageid) throws JsonProcessingException {
	boolean res = barrageService.deleteBarrage(barrageid);
	ObjectMapper objectMapper=new ObjectMapper();
	if (res) {
		String json=objectMapper.writeValueAsString(new JsonResult("删除成功"));
		return json;
	} else {
		String json=objectMapper.writeValueAsString(new JsonResult(JsonResult.ERROR, "删除失败！"));
		return json;
	}
}
@RequestMapping("/{userid}/getBarragePage")
public  Object getPage(@PathVariable String userid) throws JsonProcessingException {
	PageHelper.startPage(1,3);
	List<Barrage> barrage = barrageService.findAllBarrage(userid);
	PageInfo<Barrage> page = new PageInfo<Barrage>(barrage);
	ObjectMapper objectMapper=new ObjectMapper();
	String json=objectMapper.writeValueAsString(new JsonResult<Barrage>(page));
	return json;
}
}
