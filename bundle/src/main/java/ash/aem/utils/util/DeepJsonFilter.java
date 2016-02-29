package ash.aem.utils.util;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.sling.ResourceTraversor;

import org.apache.sling.api.request.RecursionTooDeepException;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.sling.JsonObjectCreator;
import org.apache.sling.api.resource.ValueMap;
import java.util.*;
import java.io.*;


public class DeepJsonFilter {

    private static final String[] filterKeys = new String[] {
                    "jcr:createdBy", "jcr:lastModifiedBy", "jcr:created", "jcr:lastModified", "cq:lastReplicationAction", 
                    "cq:lastReplicatedBy", "cq:lastReplicated", "cq:lastModified", "cq:lastModifiedBy", "jcr:baseVersion", 
                    "jcr:uuid", "jcr:versionHistory"
                };

    public static JSONObject clean(JSONObject jobj) throws JSONException {

        Iterator<String> jKeys = jobj.keys();
        while (jKeys.hasNext()) {
            String key = jKeys.next();
            Object val = jobj.get(key);


            System.out.println("DeepJsonFilter .val  -> "+val + " Class : " + val.getClass());

            if (val instanceof java.lang.String) {
                String sVal = (String) val;
                if("''".equals(sVal)) {
                    jobj.put(key, "");
                }
            } else if (val instanceof java.lang.String[]) {


                System.out.println("DeepJsonFilter .String[]  -> "+val);


                String[] arr = (String[]) val;
                if(arr.length == 1 && arr[0].length() < 1) {
                    jobj.put(key, new String[0]);
                }

            } else if (val instanceof org.apache.sling.commons.json.JSONArray) {


                System.out.println("DeepJsonFilter .JSONArray[]  -> "+val);


                JSONArray arr = (JSONArray) val;
                boolean empty = true;

                for(int i = 0; i< arr.length(); i++) {

                    if(arr.getString(0).length() > 0) {
                        empty = false;
                        break;
                    }
                }

                if(empty) {
                    jobj.put(key, new JSONArray());
                }

                /*
                if(arr.length() == 1 && arr.getString(0).length() < 1) {
                    jobj.put(key, new JSONArray());
                }
                */

            }
        }
        // remove filtered keys
        for (String key : filterKeys) {
            if(jobj.has(key)) {
                jobj.remove(key);
            }
        }


        return jobj;
    }

}
