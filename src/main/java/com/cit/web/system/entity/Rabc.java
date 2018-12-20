package com.cit.web.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Rabc implements Serializable
{
    private Integer id;

    private Integer pid;

    private String name;

    private String url;

    private String icon;

    private Integer type;

    private Integer orders;
}
