package com.netease.ipet;

import javax.servlet.http.HttpServletResponse;

public class ReturnUtil
{
    public static void writeBack(HttpServletResponse response, String returnvalue)
    {
        try
        {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(returnvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}