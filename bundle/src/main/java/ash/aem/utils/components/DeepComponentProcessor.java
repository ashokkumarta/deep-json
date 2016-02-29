package ash.aem.utils.components;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.api.resource.Resource;

import ash.aem.utils.components.impl.DeepTeaser;
import ash.aem.utils.components.impl.DeepMultiRef;

public class DeepComponentProcessor {

    private static final String TEASER = "cq/personalization/components/teaser";
    private static final String MULTIREF = "DTV/components/content/shared/general/multireference";

    public static JSONObject process(Resource res, JSONObject jobj, String trackingUrl) throws JSONException {

        String compType = null;
        JSONObject processedJson = null;

        try {

            compType = jobj.getString("sling:resourceType");
        } catch (JSONException ex) {
            //ignore
        }

        if(TEASER.equals(compType)) {

            DeepTeaser teaser = new DeepTeaser(res, jobj, trackingUrl);
            teaser.process();
            processedJson = teaser.getJson();

        } else if(MULTIREF.equals(compType)) {

            DeepMultiRef multiref = new DeepMultiRef(res, jobj, trackingUrl);
            multiref.process();
            processedJson = multiref.getJson();

        } else {
            processedJson = jobj;
        }

        return processedJson;
    }

}
