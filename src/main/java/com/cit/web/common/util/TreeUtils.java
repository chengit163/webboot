package com.cit.web.common.util;

import com.cit.web.common.entity.Tree;
import com.cit.web.system.entity.Rabc;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TreeUtils
{


    public static <T> void buildTree(Tree<T> root, List<Tree<T>> list)
    {
        for (Tree<T> children : list)
        {
            if (StringUtils.equals(root.getId(), children.getPid()))
            {
                root.add(children);
                continue;
            }
            for (Tree<T> parent : list)
            {
                if (StringUtils.equals(parent.getId(), children.getPid()))
                {
                    parent.add(children);
                    continue;
                }
            }
        }
    }

    private static Tree<Rabc> getTree(Rabc rabc, boolean hasContent)
    {
        Tree<Rabc> tree = new Tree<Rabc>();
        tree.setContent(hasContent ? rabc : null);
        tree.setId(rabc.getId().toString());
        tree.setPid(rabc.getPid().toString());
        tree.setName(rabc.getName());
        return tree;
    }


    public static Tree<Rabc> formatTree(List<Rabc> list)
    {
        Tree<Rabc> root = new Tree<Rabc>();
        root.setId("0");
        root.setPid("0");
        if (list != null)
        {
            List<Tree<Rabc>> trees = new ArrayList<Tree<Rabc>>();
            for (Rabc item : list)
            {
                Tree<Rabc> tree = getTree(item, true);
                trees.add(tree);
            }
            TreeUtils.buildTree(root, trees);
        }
        return root;
    }


    public static Tree<Rabc> formatTree(List<Rabc> list, Set<Integer> ids)
    {
        Tree<Rabc> root = new Tree<Rabc>();
        root.setId("0");
        root.setPid("0");
        if (list != null)
        {
            List<Tree<Rabc>> trees = new ArrayList<Tree<Rabc>>();
            for (Rabc item : list)
            {
                Tree<Rabc> tree = getTree(item, false);
                if (ids != null)
                    tree.setChecked(ids.contains(item.getId()));
                trees.add(tree);
            }
            TreeUtils.buildTree(root, trees);
        }
        return root;
    }


}
