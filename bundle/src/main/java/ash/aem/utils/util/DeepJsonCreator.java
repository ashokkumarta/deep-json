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
import org.apache.sling.commons.json.sling.JsonObjectCreator;
import org.apache.sling.api.resource.ValueMap;

import ash.aem.utils.components.DeepComponentProcessor;

import java.util.*;
import java.io.*;


public class DeepJsonCreator {

    protected String trackingUrl;

    protected Stack<String> nodePath; 

    public DeepJsonCreator(String trackingUrl) {
        this.trackingUrl = trackingUrl;
        nodePath = new Stack<String>();
    }



    public JSONObject process(Resource res) throws JSONException {

        //Validate if input is valid
        if (null == res) {
            // return empty JSON
            return new JSONObject();
        }


        //Validate if resource is valid:: Not Required...
        String resName = ResourceUtil.getName(res);
        if (null == resName) {
            // return empty JSON
            return new JSONObject();
        }

        //Process child nodes and return resulting JSON
        return getChildJson(res);
    }


    protected JSONObject getChildJson(Resource res) throws JSONException {

        nodePath.push(ResourceUtil.getName(res));
        JSONObject currObj = create(res);

        currObj = DeepComponentProcessor.process(res, currObj, this.trackingUrl);

        Iterator<Resource> children = ResourceUtil.listChildren(res);
        while(children.hasNext()) {
            Resource nextRes = children.next();
            String nextKey = ResourceUtil.getName(nextRes);
            JSONObject nextObj = process(nextRes);
            //System.out.println("Nextkey : "+nextKey);
            if(null != nextObj) {
                currObj.put(nextKey, nextObj);
                //System.out.println("Nextkey not null: "+nextKey);
            }
        }
        nodePath.pop();
        return currObj;
    }

    protected JSONObject create(Resource res) throws JSONException {

        // create json object
        JSONObject jobj = JsonObjectCreator.create(res, 0);
        return DeepJsonFilter.clean(jobj);
    }


    public JSONObject postProcess(JSONObject jobj) throws JSONException {
        //override in subclasses
        jobj.put("status", "success");
        return jobj;
    }

    public Resource preProcess(Resource res) {

        return res;
    }

}
