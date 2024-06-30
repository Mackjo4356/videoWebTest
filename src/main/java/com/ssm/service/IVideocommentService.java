package com.ssm.service;

import com.ssm.entity.StanVideoComm;
import com.ssm.entity.Videocomment;

import java.util.List;



public interface IVideocommentService {
	List<Videocomment> findAllVideocomment(String userid);
	boolean deleteVideocomment(int commentid);
	List<StanVideoComm> uniteVideoComm(String userid);
}
