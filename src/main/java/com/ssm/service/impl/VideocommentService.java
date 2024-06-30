package com.ssm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ssm.dao.VideocommentMapper;
import com.ssm.entity.StanVideoComm;
import com.ssm.entity.Videocomment;
import com.ssm.service.IVideocommentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;


@Service
public class VideocommentService implements IVideocommentService {
private VideocommentMapper videocommentMapper;

	public VideocommentMapper getVideocommentMapper() {
	return videocommentMapper;
}
@Resource
public void setVideocommentMapper(VideocommentMapper videocommentMapper) {
	this.videocommentMapper = videocommentMapper;
}

	@Override
	public List<Videocomment> findAllVideocomment(String userid) {
		return videocommentMapper.selectById(userid);
	}
	@Override
	public boolean deleteVideocomment(int commentid) {	
		return videocommentMapper.deleteByPrimaryKey(commentid)>0;
	}
	@Override
	public List<StanVideoComm> uniteVideoComm(String userid) {
		return videocommentMapper.selectUnite(userid);
	}

}
