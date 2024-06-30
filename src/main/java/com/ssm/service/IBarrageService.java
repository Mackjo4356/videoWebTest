package com.ssm.service;

import com.ssm.entity.Barrage;

import java.util.List;




public interface IBarrageService {
	List<Barrage> findAllBarrage(String userid);
	boolean deleteBarrage(int barrageid);
	List<Barrage> uniteBarrage(String userid);
}
