package EngineSkill.K8s;


import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceList;
import io.fabric8.kubernetes.api.model.NamespaceList;

import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class CRDTest {
    private static final Logger log= LoggerFactory.getLogger(CRDTest.class);

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
            //crd的关键属性,也可以定义成实体类
            CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                    .Builder()
                    .withName("flinkdeployments.flink.apache.org")
                    .withGroup("flink.apache.org")
                    .withKind("FlinkDeployment")
                    .withScope("Namespaced")
                    .withVersion("v1beta1")
                    .withPlural("flinkdeployments")
                    .build();
            //crd操作引擎
            MixedOperation<
                    GenericKubernetesResource,
                    GenericKubernetesResourceList,
                    Resource<GenericKubernetesResource>
                    > resourceMixedOperation =  client.genericKubernetesResources(context);
            //之后就与正常k8s资源操作api一致，此处是创建
            resourceMixedOperation
                    .inNamespace("default")
                    .load(new File("src/main/resources/k8s/a.yaml"))
                    .createOrReplace();

        }catch (Exception e){
            log.error("Exception:\t",e);
        }





    }

}
