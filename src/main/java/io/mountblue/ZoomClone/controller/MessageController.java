package io.mountblue.ZoomClone.controller;

import io.openvidu.java.client.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {
    private OpenVidu openVidu;
    private String OPENVIDU_URL;
    private String SECRET;

    public MessageController(@Value("${openvidu.url}") String OPENVIDU_URL, @Value("${openvidu.secret}") String SECRET) {
        this.OPENVIDU_URL = OPENVIDU_URL;
        this.SECRET = SECRET;

    }

    /*******************/
    /** Messaging API **/

    @RequestMapping(value = "/get/connections", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getConnections(@RequestParam("sessionId") String sessionIdParam) throws ParseException {

        System.out.println("Session Id : = " + sessionIdParam);
        System.out.println("All sessions " + this.openVidu.getActiveSessions());
        for (Session s : this.openVidu.getActiveSessions()) {
            System.out.println(s.getSessionId() + " | " + sessionIdParam);
            if (s.getSessionId().equals(sessionIdParam)) {
                System.out.println("Got the session : = " + s);
                return new ResponseEntity<>(this.sessionToJson(s), HttpStatus.OK);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected JSONObject sessionToJson(Session session) {
        JSONObject json = new JSONObject();
        json.put("sessionId", session.getSessionId());
        json.put("customSessionId", session.getProperties().customSessionId());
        json.put("recording", session.isBeingRecorded());
        json.put("mediaMode", session.getProperties().mediaMode());
        json.put("recordingMode", session.getProperties().recordingMode());
        json.put("defaultRecordingLayout", session.getProperties().defaultRecordingLayout());
        json.put("defaultCustomLayout", session.getProperties().defaultCustomLayout());
        JSONObject connections = new JSONObject();
        connections.put("numberOfElements", session.getConnections().size());
        JSONArray jsonArrayConnections = new JSONArray();
        session.getConnections().forEach(con -> {
            JSONObject c = new JSONObject();
            c.put("connectionId", con.getConnectionId());
            c.put("role", con.getRole());
            c.put("token", con.getToken());
            c.put("clientData", con.getClientData());
            c.put("serverData", con.getServerData());
            JSONArray pubs = new JSONArray();
            con.getPublishers().forEach(p -> {
                JSONObject jsonP = new JSONObject();
                jsonP.put("streamId", p.getStreamId());
                jsonP.put("hasAudio", p.hasAudio());
                jsonP.put("hasVideo", p.hasVideo());
                jsonP.put("audioActive", p.isAudioActive());
                jsonP.put("videoActive", p.isVideoActive());
                jsonP.put("frameRate", p.getFrameRate());
                jsonP.put("typeOfVideo", p.getTypeOfVideo());
                jsonP.put("videoDimensions", p.getVideoDimensions());
                pubs.add(jsonP);
            });
            JSONArray subs = new JSONArray();
            con.getSubscribers().forEach(s -> {
                subs.add(s);
            });
            c.put("publishers", pubs);
            c.put("subscribers", subs);
            jsonArrayConnections.add(c);
        });
        connections.put("content", jsonArrayConnections);
        json.put("connections", connections);
        return json;
    }
}
