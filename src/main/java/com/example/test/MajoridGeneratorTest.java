package com.example.test;

import com.example.demo.bean.generators.UseridGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MajoridGeneratorTest {

    @Test
    public void testMajoridGenerator(){
        List<Integer> list = new ArrayList<>();
        list.add(null);list.add(null);list.add(null);list.add(null);list.add(null);
        list.removeIf(Objects::isNull);
    }
}
