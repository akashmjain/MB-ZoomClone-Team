package io.mountblue.ZoomClone.controller;

import io.openvidu.java.client.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class RecordingController {

    // OpenVidu object as entrypoint of the SDK
    private OpenVidu openVidu;
    // Collection to pair session names and recording objects
    private Map<String, Boolean> sessionRecordings = new ConcurrentHashMap<>();
    // URL where our OpenVidu server is listening
    private String OPENVIDU_URL;
    // Secret shared with our OpenVidu server
    private String SECRET;

    public RecordingController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    /*******************/
    /** Recording API **/
    /*******************/
    @RequestMapping(value = "/recording/start", method = RequestMethod.POST)
    public ResponseEntity<?> startRecording(@RequestBody String param) throws ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(param);

        String sessionId = (String) json.get("session");
        Recording.OutputMode outputMode = Recording.OutputMode.valueOf((String) json.get("outputMode"));
        boolean hasAudio = (boolean) json.get("hasAudio");
        boolean hasVideo = (boolean) json.get("hasVideo");

        RecordingProperties properties = new RecordingProperties.Builder().outputMode(outputMode).hasAudio(hasAudio)
                .hasVideo(hasVideo).build();

        System.out.println("Starting recording for session " + sessionId + " with properties {outputMode=" + outputMode
                + ", hasAudio=" + hasAudio + ", hasVideo=" + hasVideo + "}");

        try {
            Recording recording = this.openVidu.startRecording(sessionId, properties);
            this.sessionRecordings.put(sessionId, true);
            return new ResponseEntity<>(recording, HttpStatus.OK);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/recording/stop", method = RequestMethod.POST)
    public ResponseEntity<?> stopRecording(@RequestBody String param) throws ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(param);
        String recordingId = (String) json.get("recording");

        System.out.println("Stoping recording | {recordingId}=" + recordingId);

        try {
            Recording recording = this.openVidu.stopRecording(recordingId);
            this.sessionRecordings.remove(recording.getSessionId());
            return new ResponseEntity<>(recording, HttpStatus.OK);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/recording/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRecording(@RequestBody String param) throws ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(param);
        String recordingId = (String) json.get("recording");

        System.out.println("Deleting recording | {recordingId}=" + recordingId);

        try {
            this.openVidu.deleteRecording(recordingId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/recording/get/{recordingId}", method = RequestMethod.GET)
    public ResponseEntity<?> getRecording(@PathVariable(value = "recordingId") String recordingId) {

        System.out.println("Getting recording | {recordingId}=" + recordingId);

        try {
            Recording recording = this.openVidu.getRecording(recordingId);
            return new ResponseEntity<>(recording, HttpStatus.OK);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/recording/list", method = RequestMethod.GET)
    public ResponseEntity<?> listRecordings() {

        System.out.println("Listing recordings");

        try {
            List<Recording> recordings = this.openVidu.listRecordings();

            return new ResponseEntity<>(recordings, HttpStatus.OK);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

