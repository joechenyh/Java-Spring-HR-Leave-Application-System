package sg.edu.iss.sa50.t8.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import sg.edu.iss.sa50.t8.model.BlockedLeaves;

@Service
public class JsonParsingService implements ParsingService {
	
	@Override
	public List<BlockedLeaves> parse(String url) throws Exception {
		// TODO Auto-generated method stub
		
		
		List<BlockedLeaves> blockedLeaves = new ArrayList<>();
		BlockedLeaves blockedLeave = new BlockedLeaves();
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		
		//feed JSON response as String
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		} in.close();
		
		JSONObject obj_JSONObject = new JSONObject(response.toString());
		
		JSONArray obj_JSONArray = obj_JSONObject.getJSONArray("holidays");
		
		//Loop through JSONArray, find Date and Name -> append to List<BlockedLeaves>
		for(int i = 0, size = obj_JSONArray.length(); i<size; i++) {
			JSONObject objInArray = obj_JSONArray.getJSONObject(i);
			Iterator key = objInArray.keys();
			while (key.hasNext()) {
				String k = key.next().toString();
				if(k.equals("date")) {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(objInArray.getString(k));
					blockedLeave.setDate(date);
				}
				else if(k.equals("name")) {
					blockedLeave.setName(objInArray.getString(k));
					//Added new instantiation of BlockedLeaves to prevent list from adding the "same" object
					BlockedLeaves entry = new BlockedLeaves();
					entry.setName(blockedLeave.getName());
					entry.setDate(blockedLeave.getDate());
					blockedLeaves.add(entry);
				}
			}
		}
		blockedLeaves.stream().forEach(System.out::println);
		return blockedLeaves;
		
	}

}
