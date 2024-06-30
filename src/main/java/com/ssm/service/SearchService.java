package com.ssm.service;
import com.ssm.entity.Video;

import java.util.List;


public interface SearchService {
	List<Video> findBytype(String MorA, Integer typeid, String namelike);
	List<Video> findByName(String name);
	List<Video> findByAuthor(String Author);
}
