package com.cit.web.common.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public final class Tree<T>
{
    private T content;

    private List<Tree<T>> children;

    private String id;

    private String pid;

    private String name;

    private boolean expanded;

    private boolean checked;

    public void add(Tree<T> node)
    {
        if (children == null)
            children = new ArrayList<Tree<T>>();
        children.add(node);
    }

    public void remove(Tree<T> node)
    {
        if (children != null)
        {
            children.remove(node);
        }
    }
}
