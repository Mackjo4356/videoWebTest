package com.ssm.controller;

import java.util.List;

import javax.annotation.Resource;

import com.ssm.entity.Submission;
import com.ssm.entity.Video;
import com.ssm.service.IUsersVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class UsersvideoController {
	@Autowired
	private IUsersVideoService usersvideoService;

	public IUsersVideoService getusersvideoService() {
		return usersvideoService;
	}

	@Resource
	public void setUsersVideoService(IUsersVideoService usersvideoService) {
		this.usersvideoService = usersvideoService;
	}
	@RequestMapping("/customer/{page}/{type}/{userid}/submission")
	public String contribute(@PathVariable int page, @PathVariable String type,@PathVariable String userid, Model model) {
		System.out.println(page);
		if (type.equals("0")) {
			PageHelper.startPage(page, 2);
			List<Submission> sub = usersvideoService.findusbytu("未通过",userid);
			PageInfo<Submission> pageInfo = new PageInfo<Submission>(sub);
			int pages = pageInfo.getPages();
			boolean isfirst = pageInfo.isIsFirstPage();
			boolean islast = pageInfo.isIsLastPage();
			model.addAttribute("submission", sub);
			model.addAttribute("length", sub.size());
			model.addAttribute("pages", pages);
			model.addAttribute("pageNum", page);
			model.addAttribute("isfirst", isfirst);
			model.addAttribute("islast", islast);
			model.addAttribute("typeid", 0);
			model.addAttribute("userid", userid);
			return "customer/videounpass.jsp";
		} else if(type.equals("1")){
			PageHelper.startPage(page, 2);
			List<Video> video = usersvideoService.findvbyu(userid);
			PageInfo<Video> pageInfo = new PageInfo<Video>(video);
			int pages = pageInfo.getPages();
			boolean isfirst = pageInfo.isIsFirstPage();
			boolean islast = pageInfo.isIsLastPage();
			model.addAttribute("video", video);
			model.addAttribute("length", video.size());
			model.addAttribute("pages", pages);
			model.addAttribute("pageNum", page);
			model.addAttribute("isfirst", isfirst);
			model.addAttribute("islast", islast);
			model.addAttribute("typeid", 1);
			model.addAttribute("userid", userid);
			return "customer/videopass.jsp";
		} 
		else if(type.equals("2")){
			PageHelper.startPage(page, 2);
			List<Submission> sub = usersvideoService.findusbytu("待审核",userid);
			PageInfo<Submission> pageInfo = new PageInfo<Submission>(sub);
			int pages = pageInfo.getPages();
			boolean isfirst = pageInfo.isIsFirstPage();
			boolean islast = pageInfo.isIsLastPage();
			model.addAttribute("submission", sub);
			model.addAttribute("length", sub.size());
			model.addAttribute("pages", pages);
			model.addAttribute("pageNum", page);
			model.addAttribute("isfirst", isfirst);
			model.addAttribute("islast", islast);
			model.addAttribute("typeid", 2);
			model.addAttribute("userid", userid);
			return "customer/videopassing.jsp";
		} 
		return "";
	}
	@RequestMapping("/customer/{videoid}/delvideo")
	public String delvideo(@PathVariable String videoid, Model model) {
		boolean res = usersvideoService.deleteVideo(videoid);
		return "redirect:/customer/templ.jsp";
	}
	@RequestMapping("/customer/{sid}/delsub")
	public String delsub(@PathVariable int sid, Model model) {
		boolean res = usersvideoService.deleteSubmission(sid);
		return "redirect:/customer/templ.jsp";
	}
}
