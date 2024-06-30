package com.ssm.service;

import com.ssm.entity.Usercollection;
import com.ssm.entity.Video;

import java.util.List;

public interface IUserCollectionService {
	List<Integer> videoidlist(String userid);
	List<Video> findbyvid(String userid);
	Video findvbyi(String vid);
	List<Usercollection> findAllUsercollection(String userid);
	boolean deleteUsercollection(String userid, String collection);
	boolean addUsercollection(Usercollection ucollection);
	boolean delUsercollection(String userid, String videoid);
}
