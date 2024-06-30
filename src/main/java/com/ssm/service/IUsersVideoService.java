package com.ssm.service;

import com.ssm.entity.Barrage;
import com.ssm.entity.Submission;
import com.ssm.entity.Video;
import com.ssm.entity.Videocomment;

import java.util.List;



public interface IUsersVideoService {
	List<Submission> findusbytu(String userid, String state);
	List<Video> findvbyu(String userid);
	List<Video> findvbyid(int videoid);
	boolean delcollectionbyv(String vid);
	boolean deleteVideo(String videoid);
	List<Barrage> findbarrage(int videoid);
	boolean deleteBarrage(int bagid);
	List<Videocomment> findcomment(int videoid);
	List<Submission> findsbyv(String videourl);
	boolean deletesbyv(String videourl);
	boolean deleteComment(String comid);
	boolean deleteSubmission(int sid);
}
