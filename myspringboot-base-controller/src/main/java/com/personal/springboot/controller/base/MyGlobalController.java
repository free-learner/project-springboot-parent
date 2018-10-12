package com.personal.springboot.controller.base;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.entity.BaseEntity;
import com.personal.springboot.controller.vo.ResultInfo;

/**
 * 异常跳转页面定义
 * 后续更新为JSON格式异常码数据
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年3月28日
 */
@Controller
public class  MyGlobalController extends BaseController<BaseEntity,MyGlobalController>{

	public static final String INDEX = "/index";
	public static final String ERROR_PATH = "/error/";
	public static final String ERROR_400 = ERROR_PATH+"400";
	public static final String ERROR_401 = ERROR_PATH+"401";
	public static final String ERROR_404 = ERROR_PATH+"404";
	public static final String ERROR_405 = ERROR_PATH+"405";
	public static final String ERROR_500 = ERROR_PATH+"500";
	public static final String NOAUTH = ERROR_PATH+"noauth";
	public static final String NOHEADER = ERROR_PATH+"noheader";
	public static final String TOKEN_EXPIRED = ERROR_PATH+"expired";
	public static final String TOKEN_ONCE = ERROR_PATH+"oncetoken";
	
	@RequestMapping(INDEX)
	public String handleIndex() {
		return INDEX;
	}
	
	@RequestMapping(ERROR_PATH)
	public String handleError() {
		return ERROR_PATH+"error";
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_400,produces="application/json;charset=UTF-8")
	public Object handleError400(HttpServletResponse response) {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_400,  
				getMessage(ErrorCodeConstant.ERROR_CODE_400));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_401,produces="application/json;charset=UTF-8")
	public Object handleError401(HttpServletResponse response) {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_401,  
				getMessage(ErrorCodeConstant.ERROR_CODE_401));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_404,produces="application/json;charset=UTF-8")
	public Object handleError404(HttpServletResponse response) {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_404,  
				getMessage(ErrorCodeConstant.ERROR_CODE_404));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_405,produces="application/json;charset=UTF-8")
	public Object handleError405(HttpServletResponse response) {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_405,  
				getMessage(ErrorCodeConstant.ERROR_CODE_405));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_500,produces="application/json;charset=UTF-8")
	public Object handleError500(HttpServletResponse response) {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_500,  
				getMessage(ErrorCodeConstant.ERROR_CODE_500));
	}
	
	@ResponseBody
	@RequestMapping(value = NOHEADER)
	public Object handleNoheader(HttpServletResponse response) {
	    response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
	    return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_NOHEADER,  
	            getMessage(ErrorCodeConstant.ERROR_CODE_NOHEADER));
	}
	
	@ResponseBody
	@RequestMapping(value = NOAUTH)
	public Object handleNoAuth(HttpServletResponse response) {
	    response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
	    return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_NOAUTH,  
	            getMessage(ErrorCodeConstant.ERROR_CODE_NOAUTH));
	}
	
	@ResponseBody
	@RequestMapping(value = TOKEN_EXPIRED)
	public Object handleTokenExpired(HttpServletResponse response) {
	    response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
	    return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_TOKEN_EXPIRED,  
	            getMessage(ErrorCodeConstant.ERROR_CODE_TOKEN_EXPIRED));
	}
	
    @ResponseBody
    @RequestMapping(value = TOKEN_ONCE)
    public Object handleTokenOnce(HttpServletResponse response) {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        return new ResultInfo<String>(ErrorCodeConstant.ERROR_CODE_99995,
                getMessage(ErrorCodeConstant.ERROR_CODE_99995));
    }
	   
    /**
     * 跳转到其他链接
     * http://localhost:8080/service/redirect/inner/goto?url=www.baidu.com
     */
    @RequestMapping(value = "/redirect/inner/goto", method = RequestMethod.GET)
    public ModelAndView  innerGoto(ModelAndView view,String url) {
        getLogger().info("跳转url信息为:{}", url);
        view.setViewName(url);
        view.addObject("test", "test");
        return view;
    }
    
    /**
     * http://localhost:8080/service/redirect/outter/goto?url=www.baidu.com
     */
    @RequestMapping(value = "/redirect/outter/goto", method = RequestMethod.GET)
    public ModelAndView  outterGoto(ModelAndView view,String url) {
        getLogger().info("跳转url信息为:{}", url);
        // return new ModelAndView("www/go_to", "url", url);
        //RedirectView redirectView = new RedirectView("/index{id}");
        RedirectView redirectView = new RedirectView("redirect:http://"+url);
        redirectView.setExpandUriTemplateVariables(false);
        redirectView.setExposeModelAttributes(false);
        view.setView(redirectView);
        view.addObject("test", "test");
        return view;
    }
    
    @RequestMapping(value = "/redirect/outter/goto2", method = RequestMethod.GET)
    public RedirectView  outterGoto2(String url) {
        getLogger().info("跳转url信息为:{}", url);
        // return new ModelAndView("www/go_to", "url", url);
        //RedirectView redirectView = new RedirectView("/index{id}");
        RedirectView redirectView = new RedirectView(url);
        redirectView.setExpandUriTemplateVariables(false);
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }

}