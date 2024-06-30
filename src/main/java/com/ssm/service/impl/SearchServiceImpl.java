package com.ssm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ssm.dao.VideoMapper;
import com.ssm.entity.Video;
import com.ssm.entity.VideoExample;
import com.ssm.service.SearchService;
import org.springframework.stereotype.Service;


@Service
public class SearchServiceImpl implements SearchService {

	private VideoMapper videoMapper;


	public VideoMapper getVideoMapper() {
		return videoMapper;
	}
	@Resource
	public void setVideoMapper(VideoMapper videoMapper) {
		this.videoMapper = videoMapper;
	}


	@Override
	public List<Video> findBytype(String MorA, Integer typeid, String namelike) {
		VideoExample ve=new VideoExample();
		if(namelike.equals("%all%")){
			if(MorA.equals("m1"))
				ve.createCriteria().andTypeidEqualTo(typeid);
			else
				ve.createCriteria().andTypeidEqualTo(typeid);
		}else{
			if(MorA.equals("m1"))
				ve.createCriteria().andVideonameLike(namelike).andTypeidEqualTo(typeid);
			else
				ve.createCriteria().andAuthorLike(namelike).andTypeidEqualTo(typeid);
		}
		List<Video> videos=videoMapper.selectByExample(ve);
		return videos;
	}

	@Override
	public List<Video> findByName(String name) {
		VideoExample ve=new VideoExample();
		List<Video> videos;
		if(name.equals("%all%"))
			videos=videoMapper.selectByExample(null);
		else{
			ve.createCriteria().andVideonameLike(name);
			videos=videoMapper.selectByExample(ve);
		}
		return videos;
	}

	@Override
	public List<Video> findByAuthor(String Author) {
		VideoExample ve=new VideoExample();
		List<Video> videos;
		if(Author.equals("%all%"))
			videos=videoMapper.selectByExample(null);
		else{
			ve.createCriteria().andAuthorLike(Author);
			videos=videoMapper.selectByExample(ve);
		}
		return videos;
	}

}
