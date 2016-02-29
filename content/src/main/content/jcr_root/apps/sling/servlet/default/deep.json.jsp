<%@include file="/libs/foundation/global.jsp" %>

<%@page import="org.apache.sling.api.resource.ResourceNotFoundException"%>
<%@page import="org.apache.sling.api.resource.ResourceUtil"%>
<%@page import="ash.aem.utils.api.DeepJsonUtil"%>
<%@page import="com.day.cq.wcm.core.stats.PageViewStatistics"%>

<%

    //Constants... to be moved into a configuration 
    final String SELECTOR_MAX = "max";
    final String SELECTOR_MIN = "min";

%>

<%
    String jsonout = "";
    DeepJsonUtil jsonutil = new DeepJsonUtil();

    // cannot handle the request for missing resources
    if (ResourceUtil.isNonExistingResource(resource)) {

        // Get stubbed JSON: for poc4
        jsonout = jsonutil.dumpJsonStub(resource);

        //throw new ResourceNotFoundException(
        //    resource.getPath(), "No resource found");

    } else {

        boolean valid = false;
        boolean allowmax = false;

        //TODO: Check validity of the request url
        valid = true;


        for(String selector : slingRequest.getRequestPathInfo().getSelectors()) {
            if(SELECTOR_MAX.equals(selector)) {
                allowmax = true;
            } 
        }

        if (!valid) {
            slingResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "INVALID REQUEST");
            return;
        }


        final PageViewStatistics pwSvc = sling.getService(PageViewStatistics.class);
        String trackingURLStr = "";
        if (pwSvc!=null && pwSvc.getTrackingURI() != null) {
            trackingURLStr = pwSvc.getTrackingURI().toString();
        }

        jsonout = jsonutil.dumpJson(resource, trackingURLStr);
    }

    slingResponse.setContentType("application/json");
    slingResponse.setCharacterEncoding("UTF-8");

%>

<%=jsonout%>

