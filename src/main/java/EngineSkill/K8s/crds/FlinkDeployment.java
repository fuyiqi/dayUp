package EngineSkill.K8s.crds;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;

@Version(FlinkDeployment.VERSION)
@Group(FlinkDeployment.GROUP)
@Plural(FlinkDeployment.PLURAL)
@Kind(FlinkDeployment.KIND)
public class FlinkDeployment extends CustomResource<FlinkDeploymentSpec, Void> implements Namespaced{

    public static final String GROUP = "flink.apache.org";
    public static final String VERSION = "v1beta1";
    public static final String PLURAL = "flinkdeployments";
    public static final String KIND = "FlinkDeployment";
}
