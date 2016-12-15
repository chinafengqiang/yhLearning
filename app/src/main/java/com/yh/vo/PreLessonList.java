package com.yh.vo;

import java.util.List;

/**
 * Created by FQ.CHINA on 2016/12/12.
 */

public class PreLessonList {
    private int id;
    private String name;
    private List<PreLessonVO> pldList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PreLessonVO> getPldList() {
        return pldList;
    }

    public void setPldList(List<PreLessonVO> pldList) {
        this.pldList = pldList;
    }
}
