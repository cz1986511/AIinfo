package com.danlu.core.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.danlu.dleye.core.InfoManager;
import com.danlu.dleye.persist.base.Info;

@org.junit.runner.RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@ContextConfiguration(locations = { "classpath:config/core-config.xml",
		"classpath:config/spring-mybatis.xml" })
public class ManagerTest {
    
    private InfoManager infoManager;
	
	@Autowired
    public void setInfoManager(InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    // AreaManagerTest
	@Test
	public void testGetAreaCount() {
		int count = 1;
		Assert.assertTrue(count > 0);
	}
	
	@Test
	public void testInfoManager() {
	    List<Info> list = infoManager.getAllInfos();
	    System.out.print(list.size());
	}

}
