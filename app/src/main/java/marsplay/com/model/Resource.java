package marsplay.com.model;

import java.io.Serializable;
import java.util.Date;

public class Resource implements Serializable {
    private String localUri;
    private String name;
    private String cloudinaryPublicId;
    private String requestId;
    private String resourceType;

    public Resource() {
    }

    public String getLocalUri() {
        return localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public String getCloudinaryPublicId() {
        return cloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        this.cloudinaryPublicId = cloudinaryPublicId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
