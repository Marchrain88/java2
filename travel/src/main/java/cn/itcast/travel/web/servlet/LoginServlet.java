package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码的验证
        //获取验证码
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //比较 不区分大小写  进行判断
        session.removeAttribute("CHECKCODE_SERVER");//为了清除验证码
        if(checkcode_server==null || !checkcode_server.equalsIgnoreCase(check)){
            //验证错误  注册失败
            ResultInfo info=new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");

            //将info对象序列化json
            ObjectMapper mapper=new ObjectMapper();
            String s = mapper.writeValueAsString(info);

            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);
            return;
        }


        //获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //封装use对象
        User user=new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service 查询
        UserService service=new UserServiceImpl();
        User u = service.login(user);
        ResultInfo info=new ResultInfo();
        //判断用户对象是否为null
        if(u==null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //判断是否激活
        if(u !=null&&!"Y".equals(u.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("你尚未激活  请激活");
        }

        //判断登录成功
        if(u !=null&& "Y".equals(u.getStatus())){
            info.setFlag(true);
        }
        //相应数据
        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
