package com.ssm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ssm.dao.BarrageMapper;
import com.ssm.entity.Barrage;
import com.ssm.service.IBarrageService;
import org.springframework.stereotype.Service;


@Service
public class BarrageService implements IBarrageService {
private BarrageMapper barrageMapper;
public BarrageMapper getBarrageMapper() {
	return barrageMapper;
}
@Resource
public void setBarrageMapper(BarrageMapper barrageMapper) {
	this.barrageMapper = barrageMapper;
}
	@Override
	public List<Barrage> findAllBarrage(String userid) {
		return barrageMapper.selectById(userid);
	}
	@Override
	public boolean deleteBarrage(int barrageid) {
		return barrageMapper.deleteByPrimaryKey(barrageid)>0;
	}
	@Override
	public List<Barrage> uniteBarrage(String userid) {
		return barrageMapper.selectUnite(userid);
	}
}
