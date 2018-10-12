package com.yh.loan.front.test.dao.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.user.dao.entity.UserOperationHistory;
import com.personal.springboot.user.dao.mapper.UserOperationHistoryMapper;
import com.yh.loan.front.test.base.BaseServicesTest;

public class UserOperationHistoryHistoryMapperTest extends BaseServicesTest {

	@Autowired
	private UserOperationHistoryMapper userOperationHistoryMapper;

	@Test
	public void testSelectByPrimaryKey() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory loanUser = userOperationHistoryMapper.selectByPrimaryKey(1L);
		if (loanUser != null) {
			System.out.println("返回：" + loanUser.toString());
		} else {
			System.out.println("返回：" + "暂无数据");
		}
		System.out.println("testSelectByPrimaryKey測試执行完成了。。。");
	}

	@Test
	public void testSelectAll() {
		Assert.assertNotNull(userOperationHistoryMapper);
		List<UserOperationHistory> list = userOperationHistoryMapper.selectAll();
		System.out.println("返回：" + list.toString());
		System.out.println("testSelectAll測試执行完成了。。。");
	}

	@Test
	public void testSelectByEntity() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		// user.setCode("0");
		user.setDelFlag(false);// 0-false;1-true
		List<UserOperationHistory> list = userOperationHistoryMapper.selectByEntity(user);
		System.out.println("返回：" + list.toString());
		System.out.println("testSelectByEntity測試执行完成了。。。");
	}

	@Test
	public void testSelectByCode() {
		Assert.assertNotNull(userOperationHistoryMapper);
		String code = "1";
		UserOperationHistory loanUser = userOperationHistoryMapper.selectByCode(code);
		if (loanUser != null) {
			System.out.println("返回：" + loanUser.toString());
		} else {
			System.out.println("返回：" + "暂无数据");
		}
		System.out.println("testSelectByCode測試执行完成了。。。");
	}

	@Test
	public void testSelectCount() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		// user.setCode("0");
		user.setDelFlag(false);// 0-false;1-true
		user.setCreateBy("测试0");
		// user.setMobilePhone("181");
		int count = userOperationHistoryMapper.selectCount(user);
		System.out.println("返回条数：" + count);
		System.out.println("testSelectCountByPage測試执行完成了。。。");
	}

	@Test
	public void testInsert() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		user.setCode(UUIDGenerator.generate());
		user.setUserCode("1000");
		user.setDelFlag(false);// 0-false;1-true
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userOperationHistoryMapper.insert(user);
		System.out.println("返回：" + count);
		System.out.println("testInsert測試执行完成了。。。");
	}

	@Test
	public void testInsertSelective() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		user.setUserCode("1000");
		user.setDelFlag(false);
		user.setCode(UUIDGenerator.generate());
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userOperationHistoryMapper.insertSelective(user);
		System.out.println("返回：" + count);
		System.out.println("testInsertSelective測試执行完成了。。。");
	}

	@Test
	public void testUpdateByPrimaryKey() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		user.setId(1L);
		user.setDelFlag(false);// 0-false;1-true
		user.setUpdateBy("更新人");
		user.setUserCode("10000");
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userOperationHistoryMapper.updateByPrimaryKey(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByPrimaryKey測試执行完成了。。。");
	}

	@Test
	public void testUpdateByPrimaryKeySelective() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		user.setId(6L);
		user.setDelFlag(false);// 0-false;1-true
		user.setUpdateBy("更新人");
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userOperationHistoryMapper.updateByPrimaryKeySelective(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByPrimaryKeySelective測試执行完成了。。。");
	}

	@Test
	public void testUpdateByCode() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		user.setId(1L);
		user.setCode("0e9c08f118124a0c929106d19b3340f5");
		user.setDelFlag(false);// 0-false;1-true
		user.setCreateDate(DateTimeUtil.currentTimestamp());
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userOperationHistoryMapper.updateByCode(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByCode測試执行完成了。。。");
	}

	@Test
	public void testUpdateByCodeSelective() {
		Assert.assertNotNull(userOperationHistoryMapper);
		UserOperationHistory user = new UserOperationHistory();
		user.setCode("0e9c08f118124a0c929106d19b3340f5");
		user.setDelFlag(false);// 0-false;1-true
		user.setUpdateBy("更新人");
		user.setUpdateDate(DateTimeUtil.currentTimestamp());
		int count = userOperationHistoryMapper.updateByCodeSelective(user);
		System.out.println("返回：" + count);
		System.out.println("testUpdateByCodeSelective測試执行完成了。。。");
	}

	@Test
	public void testDeleteByPrimaryKey() {
		Assert.assertNotNull(userOperationHistoryMapper);
		Long id = 1L;
		int count = userOperationHistoryMapper.deleteByPrimaryKey(id);
		System.out.println("返回：" + count);
		System.out.println("testDeleteByPrimaryKey測試执行完成了。。。");
	}

	@Test
	public void testDeleteByCode() {
		Assert.assertNotNull(userOperationHistoryMapper);
		String code = "0e9c08f118124a0c929106d19b3340f5";
		int count = userOperationHistoryMapper.deleteByCode(code);
		System.out.println("返回：" + count);
		System.out.println("testDeleteByCode測試执行完成了。。。");
	}

	@Test
	public void testInsertBatch() {
		Assert.assertNotNull(userOperationHistoryMapper);
		List<UserOperationHistory> list = new ArrayList<UserOperationHistory>();
		for (int i = 0; i < 5; i++) {
			UserOperationHistory user = new UserOperationHistory();
			user.setUserCode("1000");
			user.setCode(UUIDGenerator.generate());
			user.setDelFlag(false);// 0-false;1-true
			user.setCreateBy("测试"+i);
			user.setCreateDate(DateTimeUtil.currentTimestamp());
			user.setUpdateDate(DateTimeUtil.currentTimestamp());
			list.add(user);
		}
		int count = userOperationHistoryMapper.insertBatch(list);
		System.out.println("返回：" + count);
		System.out.println("testInsert測試执行完成了。。。");
	}
}