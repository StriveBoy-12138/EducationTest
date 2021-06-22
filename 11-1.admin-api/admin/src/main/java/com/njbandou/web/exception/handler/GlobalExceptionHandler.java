package com.njbandou.web.exception.handler;

import com.njbandou.web.exception.Entity.ErrorResponseEntity;
import com.njbandou.web.exception.GlobalException;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Dscription: 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public Result jsonErrorHandler(HttpServletRequest request, GlobalException e) {
        logger.error("系统异常{}", e.getE() == null ? e : e.getE());
        Result result = new Result();
        result.setCode(e.getCode());
        result.setMsg(e.getMessage());
        return result;
    }


    /**
     * 捕获  RuntimeException 异常
     * TODO  如果觉得在一个 exceptionHandler 通过  if (e instanceof xxxException) 太麻烦
     * TODO  那么还可以自己写多个不同的 exceptionHandler 处理不同异常
     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponseEntity runtimeExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        logger.error("系统异常{}", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        RuntimeException exception = (RuntimeException) e;
        return new ErrorResponseEntity(500, exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponseEntity unauthorizedExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        logger.info("暂无权限{}", e);
        response.setStatus(HttpStatus.OK.value());
        return new ErrorResponseEntity(401, "暂无相关权限");
    }

    /**
     * 通用的接口映射异常处理方
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return new ResponseEntity<>(new ErrorResponseEntity(status.value(), exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            logger.error("参数转换失败，方法：" + exception.getParameter().getMethod().getName() + "，参数：" + exception.getName()
                    + ",信息：" + exception.getLocalizedMessage());
            return new ResponseEntity<>(new ErrorResponseEntity(status.value(), "参数转换失败"), status);
        }
        return new ResponseEntity<>(new ErrorResponseEntity(status.value(), "参数转换失败"), status);
    }
}
