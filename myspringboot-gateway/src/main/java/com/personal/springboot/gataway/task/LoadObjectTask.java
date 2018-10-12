package com.personal.springboot.gataway.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.personal.springboot.gataway.dao.ApiAppDao;
import com.personal.springboot.gataway.dao.ApiInterfaceDao;
import com.personal.springboot.gataway.dao.ApiSubInterfaceDao;
import com.personal.springboot.gataway.dao.entity.ApiApp;



public class LoadObjectTask implements Runnable {

	@Inject
	private ApiAppDao apiAppDao;

	@Inject
	private ApiInterfaceDao apiInterfaceDao;

	@Inject
	private ApiSubInterfaceDao apiSubInterfaceDao;
	
	int i=0;
    @Override
    public void run() {
        
    	
    	//加载 ApiApp
    	List<ApiApp> list=apiAppDao.selectAll();
    	for (ApiApp apiApp : list) {
		}
    	
    	
    	//加载ApiInterface
    	
    	
    	//加载ApiSubInterface
    	
    }
    
    public void load(){
    	ThreadPoolExecutor executor=new ThreadPoolExecutor(1);    
    	executor.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }
    
    

    public static void main(String[] args) {
    	LoadObjectTask n=new LoadObjectTask();
    	n.load();
    	
        
        //executor.shutdown();
    }
    	
    
    
    

}