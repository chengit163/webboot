package com.cit.web.common.exception;

import com.cit.web.common.constant.Code;
import com.cit.web.common.constant.R;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ApiIgnore
@RestController
public class MainsiteErrorController implements ErrorController
{
    private static final String ERROR = "/error";

    @RequestMapping(
            value = {ERROR},
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response)
    {
        int code = response.getStatus();
        if (code == 403)
        {
            return new ModelAndView("error/403");
        } else if (code == 404)
        {
            return new ModelAndView("error/404");
        } else
        {
            return new ModelAndView("error/500");
        }
    }

    @RequestMapping(ERROR)
    public R handleError(HttpServletRequest request, HttpServletResponse response)
    {
        response.setStatus(200);
        int code = response.getStatus();
        return R.bind(Code.UNKNOWN);
    }

    @Override
    public String getErrorPath()
    {
        return ERROR;
    }
}
