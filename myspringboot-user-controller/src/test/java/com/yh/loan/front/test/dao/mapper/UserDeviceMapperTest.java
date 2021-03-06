package com.yh.loan.front.test.dao.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.user.dao.entity.UserDevice;
import com.personal.springboot.user.dao.mapper.UserDeviceMapper;
import com.yh.loan.front.test.base.BaseServicesTest;

public class UserDeviceMapperTest extends BaseServicesTest {

	@Autowired
	private UserDeviceMapper userDeviceMapper;

	@Test
	public void testSelectByPrimaryKey() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice loanUser = userDeviceMapper.selectByPrimaryKey(1L);
		if (loanUser != null) {
			System.out.println("返回：" + loanUser.toString());
		} else {
			System.out.println("返回：" + "暂无数据");
		}
		System.out.println("testSelectByPrimaryKey測試执行完成了。。。");
	}

	@Test
	public void testSelectAll() {
		Assert.assertNotNull(userDeviceMapper);
		List<UserDevice> list = userDeviceMapper.selectAll();
		System.out.println("返回：" + list.toString());
		System.out.println("testSelectAll測試执行完成了。。。");
	}

	@Test
	public void testSelectByEntity() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		// user.setCode("0");
		user.setDelFlag(false);// 0-false;1-true
		List<UserDevice> list = userDeviceMapper.selectByEntity(user);
		System.out.println("返回：" + list.toString());
		System.out.println("testSelectByEntity測試执行完成了。。。");
	}

	@Test
	public void testSelectByCode() {
		Assert.assertNotNull(userDeviceMapper);
		String code = "1";
		UserDevice loanUser = userDeviceMapper.selectByCode(code);
		if (loanUser != null) {
			System.out.println("返回：" + loanUser.toString());
		} else {
			System.out.println("返回：" + "暂无数据");
		}
		System.out.println("testSelectByCode測試执行完成了。。。");
	}

	@Test
	public void testSelectCount() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		// user.setCode("0");
		user.setDelFlag(false);// 0-false;1-true
		user.setCreateBy("测试0");
		// user.setMobilePhone("181");
		int count = userDeviceMapper.selectCount(user);
		System.out.println("返回条数：" + count);
		System.out.println("testSelectCountByPage測試执行完成了。。。");
	}

	@Test
	public void testInsert() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		user.setCode(UUIDGenerator.generate());
		user.setDelFlag(false);// 0-false;1-true
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userDeviceMapper.insert(user);
		System.out.println("返回：" + count);
		System.out.println("testInsert測試执行完成了。。。");
	}

	@Test
	public void testInsertSelective() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		user.setCode(UUIDGenerator.generate());
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		user.setDelFlag(false);
		int count = userDeviceMapper.insertSelective(user);
		System.out.println("返回：" + count);
		System.out.println("testInsertSelective測試执行完成了。。。");
	}

	@Test
	public void testUpdateByPrimaryKey() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		user.setId(1L);
		user.setDelFlag(false);// 0-false;1-true
		user.setUpdateBy("更新人");
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userDeviceMapper.updateByPrimaryKey(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByPrimaryKey測試执行完成了。。。");
	}

	@Test
	public void testUpdateByPrimaryKeySelective() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		user.setId(6L);
		user.setDelFlag(false);// 0-false;1-true
		user.setUpdateBy("更新人");
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userDeviceMapper.updateByPrimaryKeySelective(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByPrimaryKeySelective測試执行完成了。。。");
	}

	@Test
	public void testUpdateByCode() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		user.setId(1L);
		user.setCode("0e9c08f118124a0c929106d19b3340f5");
		user.setDelFlag(false);// 0-false;1-true
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userDeviceMapper.updateByCode(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByCode測試执行完成了。。。");
	}

	@Test
	public void testUpdateByCodeSelective() {
		Assert.assertNotNull(userDeviceMapper);
		UserDevice user = new UserDevice();
		user.setCode("0e9c08f118124a0c929106d19b3340f5");
		user.setDelFlag(false);// 0-false;1-true
		user.setUpdateBy("更新人");
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userDeviceMapper.updateByCodeSelective(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByCodeSelective測試执行完成了。。。");
	}

	@Test
	public void testDeleteByPrimaryKey() {
		Assert.assertNotNull(userDeviceMapper);
		Long id = 1L;
		int count = userDeviceMapper.deleteByPrimaryKey(id);
		System.out.println("返回：" + count);
		System.out.println("testDeleteByPrimaryKey測試执行完成了。。。");
	}

	@Test
	public void testDeleteByCode() {
		Assert.assertNotNull(userDeviceMapper);
		String code = "0e9c08f118124a0c929106d19b3340f5";
		int count = userDeviceMapper.deleteByCode(code);
		System.out.println("返回：" + count);
		System.out.println("testDeleteByCode測試执行完成了。。。");
	}

	@Test
	public void testInsertBatch() {
		Assert.assertNotNull(userDeviceMapper);
		List<UserDevice> list = new ArrayList<UserDevice>();
		for (int i = 0; i < 5; i++) {
			UserDevice user = new UserDevice();
			user.setCode(UUIDGenerator.generate());
			user.setDelFlag(false);// 0-false;1-true
			user.setCreateBy("测试"+i);
			user.setCreateDate(DateTimeUtil.currentTimestamp());
			user.setUpdateDate(DateTimeUtil.currentTimestamp());
			list.add(user);
		}
		int count = userDeviceMapper.insertBatch(list);
		System.out.println("返回：" + count);
		System.out.println("testInsert測試执行完成了。。。");
	}
}