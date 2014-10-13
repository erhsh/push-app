package com.blackcrystalinfo.push.service;

import com.blackcrystalinfo.push.exception.PushException;

public interface IService {
	void startServcie() throws PushException;
	
	void endService();
}
