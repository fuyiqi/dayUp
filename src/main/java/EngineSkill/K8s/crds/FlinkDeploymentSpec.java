package EngineSkill.K8s.crds;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;

@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class FlinkDeploymentSpec implements KubernetesResource {

    public String toString(){
        return "";
    }
}
