package com.ssm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.entity.*;
import com.ssm.service.IUserService;
import com.ssm.service.impl.UserCollectionService;
import com.ssm.utils.GetImgUrl;
import com.ssm.utils.MD5Util;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Controller
public class UseropController {
	
	private IUserService userService;
	private UserCollectionService collectionService;

	public UserCollectionService getCollectionService() {
		return collectionService;
	}
	@Resource
	public void setCollectionService(UserCollectionService collectionService) {
		this.collectionService = collectionService;
	}
	public IUserService getUserService() {
		return userService;
	}
	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/customer/useruploadvideo", method = RequestMethod.POST)
	public String uploadvideo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
			// 1、创建一个DiskFileItemFactory工厂
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// 2、创建一个文件上传解析器
				ServletFileUpload upload = new ServletFileUpload(factory);
				// 解决上传文件名的中文乱码
				upload.setHeaderEncoding("UTF-8");
				factory.setSizeThreshold(1024 * 1024 * 500);// 设置内存的临界值为500K
				File linshi = new File("E:\\linshi");// 当超过500K的时候，存到一个临时文件夹中
				factory.setRepository(linshi);
				upload.setSizeMax(1024 * 1024 * 500);// 设置上传的文件总的大小不能超过500M
				try {
					// 1. 得到 FileItem 的集合 items
					List<FileItem> /* FileItem */items = upload.parseRequest(request);
					// 2. 遍历 items:
					String userid =  items.get(0).getString("utf-8");
					String videoname = items.get(1).getString("utf-8");
					int typeid = Integer.parseInt(items.get(2).getString("utf-8"));
					String filesize = items.get(4).getString("utf-8");
					String format = items.get(5).getString("utf-8");
					String videolength = items.get(6).getString("utf-8");					
					String fileUrl = "";
					String imgUrl = "";
					String fileName = "";
					if (!userid.equals("null")) {
						for (FileItem item : items) {
							if (!item.isFormField()) {
								fileName = item.getName();
								InputStream in = item.getInputStream();
								byte[] buffer = new byte[1024];
								int len = 0;
								fileUrl = request.getSession().getServletContext().getRealPath("/")
										+ "user_uploadfiles\\" + fileName;// 文件最终上传的位置
								OutputStream out = new FileOutputStream(fileUrl);						
								while ((len = in.read(buffer)) != -1) {
									out.write(buffer, 0, len);
								}
								out.close();
								in.close();
							}
						}
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
						String uptime = df.format(new Date()).toString();
						String hou=fileName.substring(fileName.lastIndexOf("."),fileName.length());

						File file=new File(fileUrl); //指定文件名及路径
						  String filename=file.getAbsolutePath();
						  if(filename.indexOf(".")>=0){
						   filename = filename.substring(0,filename.lastIndexOf("\\"));//文件上传路径
						  }
						  file.renameTo(new File(filename+"\\"+userid+uptime+hou)); //改名
						String str="D:\\java_code\\ssm\\testSSM\\src\\main\\java\\com\\ssm\\utils";
						  GetImgUrl.processImg(new File(filename+"\\"+userid+uptime+hou).getAbsolutePath(), str+"\\ffmpeg.exe");
						Submission submission = new Submission(userid,videoname,
								"user_uploadfiles\\"+ userid+uptime+ ".jpg", 
								"user_uploadfiles\\" + userid+uptime+hou,
								filesize, format,
								uptime, "待审核", videolength,typeid);
						if (userService.addSubmission(submission)) {
							model.addAttribute("msg", "上传成功，请耐心等待管理员审核~");
						} else {
							model.addAttribute("msg", "上传失败");
						}
					} else {
						model.addAttribute("msg", "上传失败,尚未登录");
					}
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
				return "forward:/customer/user_uploadnote.jsp";
	}
	@RequestMapping(value="/customer/getvideo",method=RequestMethod.GET) /*获取video*/
	public @ResponseBody Object getvideo(int videoid) throws JsonProcessingException {
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		Video video=userService.getVideo(videoid);
		System.out.println(video.getVideoid());
		if(video!=null){
			video.setMsg("success");
			json=objectMapper.writeValueAsString(video);
			return json;
		}else{
			Video video1=new Video();
			video1.setMsg("fail");
			json=objectMapper.writeValueAsString(video1); /*手动转化为json格式*/
			return json;
		}
	}
	@RequestMapping(value="/customer/getvideocomment",method=RequestMethod.POST) /*获取评论*/
	public @ResponseBody Object getvideocomment(@RequestBody getvideocomment comment) throws JsonProcessingException {
		List<Videocomment> list;
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		int videoid=Integer.parseInt(comment.getVideoid());
		if(comment.getPagenum()==null){
			list=userService.getVideocomment(videoid);
			}
		else{
			Integer pagenum=Integer.parseInt(comment.getPagenum());
			Integer p=pagenum/5;
			if(pagenum%5>0)
				p=p+1;
			PageHelper.startPage(p,5);
			list=userService.getVideocomment(videoid);
			PageInfo<Videocomment> page=new PageInfo<Videocomment>(list);
		}
	/*	if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				User user=userService.getUsername(list.get(i).getUserid());
				list.get(i).setUserid(user.getUsername());
			}
		}*/
		System.out.println("评论条数："+list.size());
		json=objectMapper.writeValueAsString(list);
		return json;
	}
	@RequestMapping(value="/customer/getbarrage",method=RequestMethod.GET) /*获取弹幕*/
	public @ResponseBody Object getbarrage( int videoid) throws JsonProcessingException {
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		List<Barrage> barrage=userService.getBarrage(videoid);
		json=objectMapper.writeValueAsString(barrage);
		System.out.println("弹幕条数："+barrage.size());
		return json;
	}
	@RequestMapping(value="/customer/reportbarrage",method=RequestMethod.POST) //发弹幕
	public @ResponseBody Object reportBarrage(@RequestBody Barrage1 barrageT) throws JsonProcessingException {
		int videoid1 = Integer.parseInt(barrageT.getVideoid());
		String userid1= barrageT.getUserid();
		String content1= barrageT.getContent();
		String videotime1 = barrageT.getVideotime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String uptime = df.format(new Date()).toString();
		Barrage barrage=new Barrage(videoid1,userid1,content1,videotime1+"",uptime);
		System.out.println(barrage);
		ObjectMapper objectMapper=new ObjectMapper(); //手动转化为json格式发送到前端
		String json;
		if(userid1!=null && !userid1.equals("")){
			userService.addBarrage(barrage);
			barrage.setMsg("success");
			json=objectMapper.writeValueAsString(barrage);
			return json;
		}else{
			barrage.setMsg("fail");
			json=objectMapper.writeValueAsString(barrage);
			return json;
		}
	}
	@RequestMapping(value="/customer/commentvideo",method=RequestMethod.POST) //评论视频
	public @ResponseBody Object commentvideo(@RequestBody Videocomment1 videoComment) throws JsonProcessingException {
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		int videoid= Integer.parseInt(videoComment.getVideoid());
		String userid=videoComment.getUserid();
		String content=videoComment.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String uptime = df.format(new Date()).toString();
		Videocomment comment=new Videocomment(videoid,userid,content,uptime);
		userService.addComment(comment);
		json=objectMapper.writeValueAsString(comment);
		return json;
	}
	@RequestMapping(value="/customer/getcollection",method=RequestMethod.GET) /*获取收藏*/
	public @ResponseBody Object getcollection(String userid) throws JsonProcessingException {
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		List<Usercollection> usercollections=collectionService.findAllUsercollection(userid);
		json=objectMapper.writeValueAsString(usercollections);
		return json;
	}
	@RequestMapping(value="/customer/cancelcollection",method=RequestMethod.GET) /*取消收藏*/
	public @ResponseBody String cancelcollection(String userid,String videoid) {
		Video video=userService.getVideo(Integer.parseInt(videoid));	
		video.setCollection(video.getCollection()-1);
		userService.updateCollection(video);
		if(collectionService.deleteUsercollection(userid,videoid))
			return "success";
		else
			return "fail";
	}
	@RequestMapping(value="/customer/videocollection",method=RequestMethod.GET) /*收藏*/
	public @ResponseBody String videocollection(String userid,String videoid) {
		Video video=userService.getVideo(Integer.parseInt(videoid));	
		video.setCollection(video.getCollection()+1);
		userService.updateCollection(video);
		if(collectionService.addUsercollection(new Usercollection(userid,videoid)))
			return "success";
		else
			return "fail";
	}
	@RequestMapping(value="/customer/modifyuserpic",method=RequestMethod.POST) /*修改头像*/
	public @ResponseBody User videocollection(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userid=request.getParameter("userid");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		factory.setSizeThreshold(1024 * 1024 * 500);
		File linshi = new File("E:\\linshi");
		factory.setRepository(linshi);
		upload.setSizeMax(1024 * 1024 * 500);
		User user=new User();
		try {
			List<FileItem> items = upload.parseRequest(request);				
			String fileUrl = "";
			String imgUrl = "";
			String fileName = "";
			if (!userid.equals("null")) {
				for (FileItem item : items) {
					if (!item.isFormField()) {
						fileName = item.getName();
						InputStream in = item.getInputStream();
						byte[] buffer = new byte[1024];
						int len = 0;
						fileUrl = request.getSession().getServletContext()
								.getRealPath("/")
								+ "user_pic\\" + fileName;// 文件最终上传的位置
						OutputStream out = new FileOutputStream(fileUrl);						
						while ((len = in.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
						out.close();
						in.close();
					}
				}
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyyMMddHHmmss");// 设置日期格式
				String uptime = df.format(new Date()).toString();
				String hou=fileName.substring(fileName.lastIndexOf("."),fileName.length());

				File file=new File(fileUrl); //指定文件名及路径
				  String filename=file.getAbsolutePath();
				  if(filename.indexOf(".")>=0){
				   filename = filename.substring(0,filename.lastIndexOf("\\"));//文件上传路径
				  }
				  file.renameTo(new File(filename+"\\"+userid+uptime+hou)); //改名
				  user=userService.getUser(userid);
					  if (user.getImgurl()!=null&&!user.getImgurl().equals("user_pic\\user.jpg")) {
							File file3 = new File("src\\main\\webapp\\" + user.getImgurl());
							if (file3.exists() == true) {
								file3.delete();
							}
						}
				  user.setImgurl("user_pic\\"+userid+uptime+hou);
				  userService.updateUserpic(user);
				  user.setMsg("success");
				  request.getSession().setAttribute("user", user);
			} else {
				user.setMsg("fail");
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return user;
	}
	@RequestMapping(value="/customer/modifyuserinfo",method=RequestMethod.POST) //修改用户信息
	public @ResponseBody Object modifyuserinfo(String userid,String username,String sex,String birthday,String address,String emotion,String email,String sign,HttpServletRequest request) throws JsonProcessingException {
		System.out.println(userid+username+sex+birthday+address+emotion+email+sign);
		User user=userService.getUser(userid);
		user.setUsername(username);
		user.setSex(sex);
		user.setBirthday(birthday);
		user.setAddress(address);
		user.setEmotion(emotion);
		user.setEmail(email);
		user.setSign(sign);
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		if(userid!=null){
			userService.updateUserpic(user);
			User newuser=userService.getUser(userid);
			newuser.setMsg("success");
			request.getSession().setAttribute("user", newuser);
			json=objectMapper.writeValueAsString(newuser);
			return json;
		}else {
			user.setMsg("fail");
			json=objectMapper.writeValueAsString(user);
			return json;
		}
	}
	@RequestMapping(value="/customer/modifyuserpwd",method=RequestMethod.POST) //修改用户信息
	public @ResponseBody Object modifyuserpwd(String userid,String oldpwd,String newpwd,HttpServletRequest request) throws JsonProcessingException {
		User user=userService.getUser(userid);
		ObjectMapper objectMapper=new ObjectMapper();
		String json;
		if(userid!=null){
			if(oldpwd.equals(user.getPassword())){
				user.setPassword(newpwd);
				userService.updateUserpic(user);
				user.setMsg("success");
				request.getSession().setAttribute("user", user);
			}else{
				user.setMsg("errorpwd");
			}	
		}else {
			user.setMsg("nologin");
		}
		json=objectMapper.writeValueAsString(user);
		return json;
	}
}
