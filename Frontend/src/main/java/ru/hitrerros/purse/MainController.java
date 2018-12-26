package ru.hitrerros.purse;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.hitrerros.purse.websocket.ClientMessage;
import ru.hitrerros.purse.websocket.ServerMessage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ModelAndView  printHello(HttpServletRequest request) {

        ServletContext context = request.getServletContext();

        ModelAndView modelAndView = new ModelAndView("main_window");
//        modelAndView.addObject("message", "Hello Spring MVC Framework!");
        return modelAndView;
    }



    @MessageMapping("/endpoint")
    @SendTo("/purse/processor")
    public ServerMessage send(ClientMessage message) throws Exception {
        return new ServerMessage();
     }


}
