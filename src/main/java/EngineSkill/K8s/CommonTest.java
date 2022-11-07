package EngineSkill.K8s;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonTest {
    private static final Logger log= LoggerFactory.getLogger(CommonTest.class);
    private static Config config;
    private static KubernetesClient k8sClient;
    static {
        config = new ConfigBuilder()
                .withMasterUrl("https://localhost:6443")
                .build();
        k8sClient = new DefaultKubernetesClient(config);
        }


    public static void main(String[] args) {
        //***查询各类资源***
        //k8sClient.namespaces().list().getItems().forEach(x-> log.info(x.toString()));
        //k8sClient.services().list().getItems().forEach(x-> log.info(x.getMetadata().getName()));
        //k8sClient.pods().list().getItems().forEach(x->log.info(x.getMetadata().getCreationTimestamp()));
        //***操纵各类资源***




    }

}
