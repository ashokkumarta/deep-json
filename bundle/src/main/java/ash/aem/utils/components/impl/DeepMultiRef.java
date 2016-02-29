package ash.aem.utils.components.impl;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

import ash.aem.utils.components.impl.DeepGenericComponent;
import ash.aem.utils.util.DeepJsonCreator;

import java.util.*;

public class DeepMultiRef extends DeepGenericComponent {

    public DeepMultiRef(Resource res, JSONObject value, String trackingUrl) {
        super(res, value, trackingUrl);
    }

    public void process() throws JSONException {

        JSONArray refs = new JSONArray(getString("multiref"));


        for(int i = 0; i < refs.length(); i++) {

            String refPath = refs.getString(i);
            String refName = refPath.substring(refPath.lastIndexOf("/")+1);
            Resource refItem = res.getResourceResolver().getResource(refPath);
            DeepJsonCreator jsonCreator = new DeepJsonCreator(this.trackingUrl);
            JSONObject refJson = jsonCreator.process(refItem);
            jobj.put(refName,refJson);
        }

    
    }
}
