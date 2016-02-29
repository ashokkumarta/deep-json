package ash.aem.utils.api;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.sling.ResourceTraversor;

import org.apache.sling.api.request.RecursionTooDeepException;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import ash.aem.utils.util.DeepJsonCreator;


import java.util.*;
import java.io.*;


public class DeepJsonUtil {

    public String dumpJson(Resource resource, String trackingUrl) throws JSONException {

        DeepJsonCreator jsonCreator = new DeepJsonCreator(trackingUrl);
        return jsonCreator.process(resource).toString();
            
    }
}
