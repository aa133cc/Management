package com.southwind.util;

import lombok.Data;

import java.util.List;

@Data
public class PageUtil {
    public static final Integer SIZE = 3;
    private Integer page;
    private Long total;
    private Long pages;
    private List data;
}
