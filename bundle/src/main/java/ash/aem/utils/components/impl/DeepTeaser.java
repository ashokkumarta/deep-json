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

public class DeepTeaser extends DeepGenericComponent {

    public DeepTeaser(Resource res, JSONObject value, String trackingUrl) {
        super(res, value, trackingUrl);
    }

    public void process() throws JSONException {

        String campaignpath = getString("campaignpath");

        Resource campaignRes = res.getResourceResolver().getResource(campaignpath);

        String campaignResName = ResourceUtil.getName(campaignRes);

        DeepJsonCreator jsonCreator = new DeepJsonCreator(this.trackingUrl);
        JSONObject campaignFullJson = jsonCreator.process(campaignRes);

        JSONObject campaignBaseDetails = campaignFullJson.getJSONObject("jcr:content");
        //String campaignTitle = campaignBaseDetails.getString("jcr:title");

        Iterator<String> keys = campaignFullJson.keys();

        String touchPoint = null;

        while (keys.hasNext()) {
            String key = keys.next();

            Object currObj = campaignFullJson.get(key);
            if(!(currObj instanceof JSONObject)) continue;

            JSONObject currJsonObj = (JSONObject) currObj;

            if("jcr:content".equals(key)) {

                if(null != touchPoint) continue;

                JSONArray tpoints = currJsonObj.getJSONArray("touchpoints");
                touchPoint = tpoints.getString(tpoints.length()-1);
                continue;
            }

            JSONObject campaignPromoDetails = getPromo(campaignResName, campaignpath, key, currJsonObj);
            if(jobj.has("promotion:details")) {
                jobj.accumulate("promotion:details",campaignPromoDetails);
            } else {
                jobj.append("promotion:details",campaignPromoDetails);
            }
        }

        jobj.put("strategy",getName(getString("strategyPath")));

        if(null == touchPoint) {
            touchPoint = campaignpath;
        }
        jobj.put("promo:path",touchPoint);

        jobj.put("div:id",getId(touchPoint));
        jobj.put("wcm:mode","false");
        jobj.put("tracking:url",this.trackingUrl);
        jobj.put("div:class","campaign campaign-"+campaignResName);

    }



    private JSONObject getPromo(String campaignResName, String campaignPath, String key, JSONObject jObj) throws JSONException {

        JSONObject promoJson = new JSONObject();
        promoJson.put("path",campaignPath+"/"+key);
        promoJson.put("name",key);
        promoJson.put("title",getJCRContentString(jObj, "jcr:title"));
        promoJson.put("campainName",campaignResName);
        promoJson.put("thumbnail",checkImage(jObj)?campaignPath+"/"+key+".img.png":campaignPath+"/"+key+"/jcr:content/par/image.img.png");
        promoJson.put("id",campaignResName+"_"+key);
        promoJson.put("segments",getJCRContentArray(jObj, "cq:segments"));
        promoJson.put("tags",getJCRContentArray(jObj, "cq:tags"));
        return promoJson;
    }
}
