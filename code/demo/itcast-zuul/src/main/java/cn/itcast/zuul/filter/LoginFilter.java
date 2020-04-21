package cn.itcast.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //初始化context上下文对象，servlet spring
        RequestContext context = RequestContext.getCurrentContext();

        //获取request对象
        HttpServletRequest request = context.getRequest();

        //获取参数

        String token = request.getParameter("token");
        if(StringUtils.isBlank(token)){
            //拦截,不转发请求
            context.setSendZuulResponse(false);
            //响应状态码，401-身份未认证
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            //设置响应的提示
            context.setResponseBody("requst error!");
        }

        //代表返回值为null，代表该过滤器什么都不做
        return null;
    }
}
