package EngineSkill.K8s;

import EngineSkill.K8s.crds.FlinkDeployment;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CRDTest {
    private static final Logger log= LoggerFactory.getLogger(CRDTest.class);

    public static String basicYaml = "apiVersion: flink.apache.org/v1beta1\n" +
            "kind: FlinkDeployment\n" +
            "metadata:\n" +
            "  name: basic-example\n" +
            "spec:\n" +
            "  image: flink:1.15\n" +
            "  flinkVersion: v1_15\n" +
            "  flinkConfiguration:\n" +
            "    taskmanager.numberOfTaskSlots: \"2\"\n" +
            "  serviceAccount: flink\n" +
            "  jobManager:\n" +
            "    resource:\n" +
            "      memory: \"2048m\"\n" +
            "      cpu: 1\n" +
            "  taskManager:\n" +
            "    resource:\n" +
            "      memory: \"2048m\"\n" +
            "      cpu: 1\n" +
            "  job:\n" +
            "    jarURI: local:///opt/flink/examples/streaming/StateMachineExample.jar\n" +
            "    parallelism: 2\n" +
            "    upgradeMode: stateless";


    public static void main(String[] args) {
        Config config = new ConfigBuilder()
                .withMasterUrl("https://localhost:6443")
                .build();
        try(KubernetesClient client = new DefaultKubernetesClient(config)){
            //ns列表
            NamespaceList myNs = client.namespaces().list();
            myNs.getItems().forEach(
                    namespace -> {
                        //log.info(namespace.getMetadata().getName()+":"+namespace.getStatus().getPhase());
                    }
            );
            //自定义资源pojo
            CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                    .Builder()
                    .withName("flinkdeployments.flink.apache.org")
                    .withGroup("flink.apache.org")
                    .withKind("FlinkDeployment")
                    .withScope("Namespaced")
                    .withVersion("v1beta1")
                    .withPlural("flinkdeployments")
                    .build();
            //获取集群crd列表
            CustomResourceDefinitionList crdList = client.apiextensions().v1().customResourceDefinitions().list();
            String flinkDeploymentCRDName = CustomResource.getCRDName(FlinkDeployment.class);
            CustomResourceDefinition flinkDeploymentCRD = null;
            for (CustomResourceDefinition crd :crdList.getItems()){
                ObjectMeta metadata = crd.getMetadata();
                if (metadata != null){
                    String name = metadata.getName();
                    if(flinkDeploymentCRDName.equals(name)){
                        flinkDeploymentCRD = crd;
                    }
                }
            }

        }catch (Exception e){
            log.error("Exception:\t",e);
        }








    }

}