package com.cit.web;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Webboot
{
    @Data
    static class Item
    {
        private String name;
        private String desc;
        private String github;
        private String home;
    }

    public static void main(String[] args)
    {
        List<Item> welcome = getItems("screenshots" + File.separator + "welcome.json");
        List<Item> welcome_js = getItems("screenshots" + File.separator + "welcome_js.json");
        List<Item> welcome_ui = getItems("screenshots" + File.separator + "welcome_ui.json");
        //
        //        后端技术选型
        System.out.println("+后端技术选型====================================================================================================");
        if (welcome != null)
            for (Item item : welcome)
                printHtml(item);
        System.out.println("-后端技术选型====================================================================================================");
        //
        //        前端技术选型
        System.out.println("+前端技术选型====================================================================================================");
        if (welcome_js != null)
            for (Item item : welcome_js)
                printHtml(item);
        System.out.println("-前端技术选型====================================================================================================");
        //        推荐模板
        System.out.println("+推荐模板====================================================================================================");
        if (welcome_ui != null)
            for (Item item : welcome_ui)
                printHtml(item);
        System.out.println("-推荐模板====================================================================================================");
        System.out.println();
        System.out.println("- 后端技术选型  ");
        if (welcome != null)
            for (Item item : welcome)
                printReadme(item);
        System.out.println("- 前端技术选型  ");
        if (welcome_js != null)
            for (Item item : welcome_js)
                printReadme(item);
        System.out.println("- 推荐模板  ");
        if (welcome_ui != null)
            for (Item item : welcome_ui)
                printReadme(item);
    }


    //screenshots
    private static List<Item> getItems(String filename)
    {
        try
        {
            InputStream is = new FileInputStream(filename);
            String content = IOUtils.toString(is, "utf-8");
            return JSON.parseArray(content, Item.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void printHtml(Item item)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t").append("<!--").append(item.getName()).append("-->").append("\n");
        sb.append("\t\t").append("<div class=\"clearfix\">").append("\n");
        sb.append("\t\t\t<label class=\"inline\">").append("\n");
        if (StringUtils.isEmpty(item.getGithub()))
        {
            sb.append("\t\t\t\t").append(item.getName());
        } else
        {
            sb.append("\t\t\t\t").append("<a href=\"").append(item.getGithub()).append("\" target=\"_blank\">").append(item.getName()).append("</a>");
        }
        sb.append(" (").append(item.getDesc()).append(") ").append("\n");
        if (StringUtils.isEmpty(item.getHome()))
        {
            sb.append("\t\t\t\t").append("@");
        } else
        {
            sb.append("\t\t\t\t").append("<a href=\"").append(item.getHome()).append("\" target=\"_blank\">@</a>").append("\n");
        }
        sb.append("\t\t\t").append("</label>").append("\n");
        sb.append("\t\t").append("</div>");
        System.out.println(sb.toString());
    }

    private static void printReadme(Item item)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(item.getName()).append("](").append(item.getGithub()).append(")");
        sb.append(" ");
        sb.append("(").append(item.getDesc()).append(")");
        sb.append(" ");
        sb.append("[@](").append(item.getHome()).append(")");
        sb.append("  ");
        System.out.println(sb.toString());
    }
}
