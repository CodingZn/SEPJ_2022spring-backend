package com.example.demo.exceptions;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handle1(HttpMessageNotReadableException e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "网络错误！信息格式错误！");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, Object>> handle2(Exception e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "信息格式错误！请检查输入数字的合法性！");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<Map<String, Object>> handle3(NullPointerException e){
//        Map<String, Object> map = new HashMap<>();
//        map.put("message", "缺少必要的信息条目！");
//        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler()
    public ResponseEntity<Map<String, Object>> handle4(MissingRequestHeaderException e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "网络错误！缺少必要请求头！");
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handle5(HttpRequestMethodNotSupportedException e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "网络错误！方法不被允许！");
        return new ResponseEntity<>(map, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handle6(SQLIntegrityConstraintViolationException e){
        Map<String, Object> map = new HashMap<>();
        String message;
        switch (e.getErrorCode()){
            case 1451: message = "存在依赖项，不能删除指定条目！"; break;
            case 1452: message = "您所选的依赖属性不存在，不能创建条目！"; break;
            case 1062: message = "数据与已有数据冲突，不能创建！"; break;
            default: message = "数据有误！"; break;
        }

        map.put("message", message);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handle7(ConstraintViolationException e){
        Map<String, Object> map = new HashMap<>();
        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
        map.put("message", "输入格式错误！" + msg);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handle8(EntityNotFoundException e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "无对应信息！");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<Map<String, Object>> handle9(MyException e){
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
