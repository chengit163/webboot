package com.cit.web.system.controller;

import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.Code;
import com.cit.web.common.constant.R;
import com.cit.web.common.util.AESUtils;
import com.cit.web.common.util.ShiroUtils;
import com.google.code.kaptcha.Producer;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Api(description = "登陆/登出控制器")
@Controller
public class LoginController
{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ApiIgnore
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @ApiIgnore
    @RequestMapping("/logout")
    public String logout()
    {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @SysLog("登陆")
    @ApiOperation("登陆 (登出: /logout; 验证码: /captcha)")
    @PostMapping("/login")
    @ResponseBody
    public R ajaxLogin(HttpServletRequest request, String username, String password, String code)
    {
        if (!ShiroUtils.checkCaptcha(code))
        {
            return R.bind(Code.INVALID.getCode(), "验证码不正确");
        }
        if (StringUtils.isEmpty(username))
        {
            return R.bind(Code.INVALID.getCode(), "用户名不能为空");
        }
        if (StringUtils.isEmpty(password))
        {
            return R.bind(Code.INVALID.getCode(), "密码不能为空");
        }
        try
        {
            // 请求的密码参数解密，aes的key必须为16位
            String key = code + code + code + code;
            password = AESUtils.aesDecrypt(password, key);
            //
            password = new Md5Hash(password, username).toString();
            ShiroUtils.login(username, password);
            // 登陆成功
            try
            {
                ShiroUtils.cleanCaptcha();
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                String os = userAgent.getOperatingSystem().getName();
                String browser = userAgent.getBrowser().getName() + " " + userAgent.getBrowserVersion();
                ShiroUtils.setOsBrowser(os, browser);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return R.bind(Code.SUCCESS);
        } catch (UnknownAccountException e)
        {
            throw e;
        } catch (IncorrectCredentialsException e)
        {
            throw e;
        } catch (LockedAccountException e)
        {
            throw e;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return R.bind(Code.UNKNOWN);
    }

    @Resource
    private Producer captchaProducer;

    /**
     * 获取验证码图片
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiIgnore
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码文本
        String capText = captchaProducer.createText();
        logger.debug("验证码: " + capText);
        ShiroUtils.putCaptcha(capText);
        //利用生成的字符串构建图片
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try
        {
            out.flush();
        } finally
        {
            out.close();
        }
    }

}
