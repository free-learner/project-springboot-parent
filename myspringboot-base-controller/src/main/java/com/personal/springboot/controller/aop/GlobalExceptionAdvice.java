package com.personal.springboot.controller.aop;

import java.sql.SQLException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.exception.BaseServiceException;
import com.personal.springboot.common.exception.NoAuthException;
import com.personal.springboot.controller.utils.MessageSourceUtil;
import com.personal.springboot.controller.vo.ResultInfo;

/**
 * 全局异常处理器
 * 
 * @Author LiuBao
 * @Version 2.0 2017年3月29日
 */
//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionAdvice {

	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

	@Autowired(required=false)
	private MessageSourceUtil messageSourceUtil;

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Object handleMissingServletRequestParameterException(HttpServletResponse response,MissingServletRequestParameterException exception) {
		logger.error("缺少请求参数", exception);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTPRESENT,
				messageSourceUtil.getMessage(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTPRESENT));
	}

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Object handleHttpMessageNotReadableException(HttpServletResponse response,HttpMessageNotReadableException exception) {
		logger.error("参数解析失败", exception);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_COULDNOT_READJSON,
				messageSourceUtil.getMessage(ErrorCodeConstant.REQUIRED_COULDNOT_READJSON));
	}

	/**
	 * 400 - Bad Request
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object handleBindException(HttpServletResponse response,BindException exception) {
		logger.error("参数绑定失败", exception);
		BindingResult result = exception.getBindingResult();
		FieldError error = result.getFieldError();
		String field = error.getField();
		String code = error.getDefaultMessage();
		String message = String.format("%s:%s", field, code);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_COULDNOT_BINDPARAM, message);
	}

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public Object handleServiceException(HttpServletResponse response,ConstraintViolationException exception) {
		logger.error("参数Constraint验证失败", exception);
		Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
		ConstraintViolation<?> violation = violations.iterator().next();
		String message = violation.getMessage();
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_VALIDATION_EXCEPTION, message);
	}

	/**
	 * 400 - Bad Request
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object handleValidationException(HttpServletResponse response,ValidationException exception) {
		logger.error("参数验证失败", exception);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_VALIDATION_EXCEPTION,
				messageSourceUtil.getMessage(ErrorCodeConstant.REQUIRED_VALIDATION_EXCEPTION));
	}

	/**
	 * 405 - Method Not Allowed
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Object handleHttpRequestMethodNotSupportedException(HttpServletRequest request,HttpServletResponse response,HttpRequestMethodNotSupportedException exception) {
	    String method = request.getMethod();
		logger.error("不支持当前请求{}方法:{}", exception,method);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_METHOD_NOTSUPPORTED,
				messageSourceUtil.getMessage(ErrorCodeConstant.REQUIRED_METHOD_NOTSUPPORTED,new Object[]{method}));
	}

	/**
	 * 415 - Unsupported Media Type
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public Object handleHttpMediaTypeNotSupportedException(HttpServletRequest request,HttpServletResponse response,HttpMediaTypeNotSupportedException exception) {
		String contentType = request.getContentType();
		logger.error("不支持当前媒体类型[{}],error:{}", exception,contentType);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_CONTENTTYPE_NOTSUPPORTED,
				messageSourceUtil.getMessage(ErrorCodeConstant.REQUIRED_CONTENTTYPE_NOTSUPPORTED,new Object[]{contentType}));
	}

	/**
	 * 500 - Internal Server Error
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleException(HttpServletResponse response,Exception exception) {
		logger.error("通用异常", exception);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_DEFAULT,
				messageSourceUtil.getMessage(ErrorCodeConstant.ERROR_CODE_DEFAULT));
	}

	/**
	 * 操作数据库出现异常:名称重复，外键关联
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleDataIntegrityViolationException(HttpServletResponse response,DataIntegrityViolationException exception) {
		logger.error("操作数据库更新/插入出现异常:", exception);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_10003,
				messageSourceUtil.getMessage(ErrorCodeConstant.ERROR_CODE_10003));
	}
	/**
	 * 数据库异常,请稍候重试!
	 */
	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleSQLException(HttpServletResponse response,SQLException exception) {
	    logger.error("操作数据库出现异常:", exception);
	    return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_10002,
	            messageSourceUtil.getMessage(ErrorCodeConstant.ERROR_CODE_10002));
	}

	/**
	 * 500 - Internal Server Error
	 */
	@ExceptionHandler(BaseServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleServiceException(HttpServletResponse response,BaseServiceException exception) {
		logger.error("自定义业务逻辑异常", exception);
		return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_DEFAULT, exception.getMessage());
	}
	
	/**
	 * 调用方法对应的Mapper中SQL不存在!
	 */
	@ExceptionHandler(BindingException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleBindingException(HttpServletResponse response,BindingException exception) {
	    logger.error("调用方法对应的Mapper中SQL不存在!", exception);
	    return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_10001, 
	            messageSourceUtil.getMessage(ErrorCodeConstant.ERROR_CODE_10001));
	}

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentNotValidException(HttpServletResponse response,MethodArgumentNotValidException exception) {
		logger.error("参数校验失败", exception);
		BindingResult result = exception.getBindingResult();
		FieldError error = result.getFieldError();
		String field = error.getField();
		String code = error.getDefaultMessage();
		Object rejectedValue = error.getRejectedValue();
		// 解析此处返回非法的字段名称，原始值，错误信息
		String message = String.format("%s:%s", field, code);
		return new ResultInfo<Object>(ErrorCodeConstant.ERROR_CODE_DEFAULT, message, rejectedValue);
	}
	
	/**
	 *NoAuthException 访问资源未授权
	 */
	@ExceptionHandler(NoAuthException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleNoAuthException(HttpServletResponse response,NoAuthException exception) {
	    logger.error("访问资源未授权", exception);
	    return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_NOAUTH,  
	            messageSourceUtil.getMessage(ErrorCodeConstant.ERROR_CODE_NOAUTH));
	}
	
	/**
	 * 文件上传max-request-size/spring.http.multipart.max-file-size尺寸过大
	 */
	@ExceptionHandler({MultipartException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleSizeLimitExceededException(HttpServletResponse response,MultipartException exception1) {
	    if(exception1!=null){
	        logger.error("文件上传max-request-size/spring.http.multipart.max-file-size尺寸过大,异常信息:{}",  exception1);
	    }
	    return new ResultInfo<String>().buildFailure(ErrorCodeConstant.ERROR_CODE_99990,  
	            messageSourceUtil.getMessage(ErrorCodeConstant.ERROR_CODE_99990));
	}

}