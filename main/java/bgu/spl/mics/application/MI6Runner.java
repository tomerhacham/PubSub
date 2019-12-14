package bgu.spl.mics.application;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.publishers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(args[0]));
        } catch (IOException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject json = (JSONObject) obj;
        JSONArray inv = (JSONArray) json.get("inventory");
        String[] s = MI6Runner.ConvertJSONArrayToStringArray(inv);
        Inventory.getInstance().load(s);
        MessageBroker messageBroker = MessageBrokerImpl.getInstance();

        JSONObject services = (JSONObject) json.get("services");
        Long Moneypenny = (Long) services.get("Moneypenny");
        Long M = (Long) services.get("M");
        Long time = (Long) services.get("time");
        JSONArray intelligence = (JSONArray) services.get("intelligence");
        JSONObject one = (JSONObject) intelligence.get(0);
        JSONObject two = (JSONObject) intelligence.get(1);
        JSONArray missionSet1 = (JSONArray) one.get("missions");
        JSONArray missionSet2 = (JSONArray) two.get("missions");


    }

    private static List<MissionInfo> createMissions(JSONArray info){
        LinkedList<MissionInfo> list = new LinkedList<>();
        for (Object missionInfo:info ) {
            JSONObject jsonObject = (JSONObject)missionInfo;
            MissionInfo mission = new MissionInfo();
            Long duration = (Long) jsonObject.get("duration");
            Long timeExpired = (Long) jsonObject.get("timeExpired");
            Long timeIssued = (Long) jsonObject.get("timeIssued");
            String gadget = (String) jsonObject.get("gadget");
            String missionName = (String) jsonObject.get("missionName");
            JSONArray serialsJsonArray = (JSONArray)jsonObject.get("serialAgentsNumbers");
            LinkedList<String> sn = new LinkedList<>();
            for (Object snobject:serialsJsonArray) {
                sn.add((String)snobject);
            }
            //TODO:make set for all mission detalis


            list.add(missionInfo1);
        }
        return null;

    }

    private static String[] ConvertJSONArrayToStringArray(JSONArray jsonArray){
        String[] strings = new String[jsonArray.size()];
        for(int i=0;i<jsonArray.size();i++){
            strings[i] = (String) jsonArray.get(i);
        }
        return strings;
    }
}
