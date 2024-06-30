package com.ssm.service;

import com.ssm.entity.Submission;

import java.util.List;



public interface INoPassService {
	List<Submission> findAllNoPass(String userid, String state);
	boolean deleteNoPass(int submissionid);
}
