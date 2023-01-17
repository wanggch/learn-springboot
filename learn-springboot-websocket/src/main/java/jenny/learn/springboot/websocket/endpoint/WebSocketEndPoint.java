package jenny.learn.springboot.websocket.endpoint;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket/{name}")
public class WebSocketEndPoint {

//    private static final String CLIENT_TYPE_PC = "computer";
//    private static final String CLIENT_TYPE_MOBILE = "mobile";

    private static Map<Session, String> socketMap = new ConcurrentHashMap<>();

    // 连接建立成功时触发的方法
    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) {
        socketMap.put(session, name);
        log.info("当前连接数：{}", socketMap.keySet().size());
    }

    // 关闭时触发的方法
    @OnClose
    public void onClose(Session session) {
        socketMap.remove(session);
        log.info("当前连接数：{}", socketMap.keySet().size());
    }

    // 收到客户端消息时触发的方法
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自{}的消息：{}", session, message);
        String name = socketMap.get(session);
//        Dict data = Dict.create();
//        data.set("state", 1);
        try {
            // 遍历所有建立连接的客户端，给其它客户端发送消息
            for (Session client : socketMap.keySet()) {
                if (StrUtil.equals(socketMap.get(client), name)) {
                    continue;
                }
                client.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("向客户端发送消息时发生错误：", e);
        }
    }

    // 发生错误时触发的方法
    @OnError
    public void onError(Session session, Throwable throwable) {
        socketMap.remove(session);
        log.info("当前连接数：{}", socketMap.keySet().size());
    }

//    private Dict generateData(String clientType, String message) {
//        Dict result = Dict.create();
//        Map<String, String> messageMap = new Gson().fromJson(message, Map.class);
////        if (Objects.equals(clientType, CLIENT_TYPE_PC)) {
////            result.set("readyState", messageMap.get("state"));
////        } else if (Objects.equals(clientType, CLIENT_TYPE_MOBILE)) {
////            result.set("finishState", messageMap.get("state"));
////        }
//
//        result.set("finishState", messageMap.get("state"));
//        return result;
//    }
}
