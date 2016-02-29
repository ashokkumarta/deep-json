package ash.aem.utils.components.impl;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;

import ash.aem.utils.components.DeepComponent;


public class DeepGenericComponent implements DeepComponent {

    protected JSONObject jobj;    
    protected Resource res;
    protected String trackingUrl;

    public DeepGenericComponent(Resource res, JSONObject value, String trackingUrl) {
        this.res = res;
        this.jobj = value;
        this.trackingUrl = trackingUrl;
    }

    public void process(Resource res, JSONObject jobj) {

    }

    public JSONObject getJson() {
        return this.jobj;
    }

    protected String getString(String key) {

        return getString(jobj, key);
    }

    protected Object get(String key) {

        return getString(jobj, key);
    }

    protected Object get(JSONObject jObj, String key) {

        try { 

            return jObj.get(key);
        } catch (JSONException ex) {
            // ignore
        }
        return null;
    }

    protected String getString(JSONObject jObj, String key) {

        try { 

            return jObj.getString(key);
        } catch (JSONException ex) {
            // ignore
        }
        return "";
    }
 

    protected String getJCRContentString(String key) {

        return getJCRContentString(jobj, key);
    }


    protected String getJCRContentString(JSONObject jObj, String key) {

        try { 

            return jObj.getJSONObject("jcr:content").getString(key);
        } catch (JSONException ex) {
            // ignore
        }
        return "";
    }

    protected JSONArray getJCRContentArray(String key) {

        return getJCRContentArray(jobj, key);
    }

    protected JSONArray getJCRContentArray(JSONObject jObj, String key) {

        try { 

            return jObj.getJSONObject("jcr:content").getJSONArray(key);
        } catch (JSONException ex) {
            // ignore
        }
        return new JSONArray();
    }

    protected boolean checkImage(JSONObject jObj) {

        boolean hasImage = false;
        try { 

            jObj.getJSONObject("jcr:content").getJSONObject("image").getJSONObject("file");
            hasImage = true;
        } catch (JSONException ex) {
            // ignore
        }
        return hasImage;
    }

    protected String getName(String path) {

        return path.substring(path.lastIndexOf("/")+1).replaceAll(".js","");
    }

    protected String getId(String path) {

        return path.replaceAll("/","_").replaceAll(":","_");
    }
    
}
