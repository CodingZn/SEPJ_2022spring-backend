package com.example.demo.service;

import java.util.List;

public interface GeneralService<T> {

    List<String> getAllIds();

    T getABean(String id);

    List<T> getAllBeans();

    String createABean(String id, T bean);

    String createBeans(List<T> beans);

    String changeABean(String id, T bean);

    String deleteABean(String id);

    String deleteBeans(List<?> ids);
}
