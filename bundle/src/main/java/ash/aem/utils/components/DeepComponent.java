package ash.aem.utils.components;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.api.resource.Resource;


public interface DeepComponent {

    public void process(Resource res, JSONObject jobj) throws JSONException;
    public JSONObject getJson();

}
