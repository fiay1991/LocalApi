package com.park.localapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;

/**
 * Hello world!
 *
 */
@Controller
public class App {
	
	@ResponseBody
	@RequestMapping(value = "/testController", produces = "text/html;charset=UTF-8")
	public String test(HttpServletRequest request, HttpServletResponse response) throws Exception {
   
		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));

	}
	
	
	@RequestMapping(value = "/testdddController", produces = "text/html;charset=UTF-8")
	public String testddd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		switch ("1") {
		case "cp_order_discount":
		break;
		}
		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));

	}
	
	public static void main(String args[])  {
//		String params ="{'object':{'body':'停车费','trade_no':'17102318153951437','total_fee':'0','parkid':'562','trade_type':'WEIXIN','terminal_type':'H5P','openid':'o3jdVv0Y2DRf18v9P0Xwbhyr0pYY','notify_url':'http://alphascanpay.crland.com.cn/ScanPay/scan/weixinnotify'},'result_code':2000,'token':'','content':'成功'}";
//
//		try {
//			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//			gson.toJson(params);
//			System.out.println(gson.fromJson(params, Map.class));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		//findAllThreads();
		
	}
	
	/**
     *  获取Java VM中当前运行的所有线程
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/testThread", produces = "text/html;charset=UTF-8")
    public  String  findAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slacks = new Thread[estimatedSize];
         //获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slacks);
        Thread[] threads = new Thread[actualSize];
        System.arraycopy(slacks, 0, threads, 0, actualSize);
        for (int i = 0; i < threads.length; i++) {
			System.out.println("第"+i+"个线程名称:"+threads[i].getName()+"--- 线程ID:"+threads[i].getId()+"--- 线程组:"+threads[i].getThreadGroup()+
					"--- 线程类:"+threads[i].getClass());
		}
        return ResultTools.setResponse(LocalCodeConstants.SUCCESS,LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
    }
}
