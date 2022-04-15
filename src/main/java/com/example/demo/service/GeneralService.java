package com.example.demo.service;

import java.util.List;

public interface GeneralService<T> {

    String getANewId();

    List<String> getAllIds();

    T getABean(String id);

    List<T> getAllBeans();

    String createABean(String id, T bean);

    String changeABean(String id, T bean);

    String deleteABean(String id);
}
