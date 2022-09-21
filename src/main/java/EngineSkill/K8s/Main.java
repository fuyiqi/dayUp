package EngineSkill.K8s;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.internal.RawCustomResourceOperationsImpl;

public class Main {
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
                        //System.out.println(namespace.getMetadata().getName()+":"+namespace.getStatus().getPhase());
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
            //构建crd操作引擎
            RawCustomResourceOperationsImpl crdIml = client.customResource(context);
            System.out.println(crdIml.delete("default"));

        }catch (Exception e){
            System.out.println(e);
        }








    }

}
