package EngineSkill.Kubernetes;


import io.kubernetes.client.openapi.models.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static String kubeConfigPath = "/Users/fuyiqi/IdeaProjects/dayUp/src/main/resources/k8s/kube_config";

    public static void main(String[] args) {
        getAllPodListTest();

    }

    private static void getAllPodListTest(){
        if (!new File(kubeConfigPath).exists()) {
            System.out.println("kubeConfig不存在，跳过");
            return;
        }
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        V1PodList podList = k8sClient.getAllPodList();
        for (V1Pod item : podList.getItems()) {
            System.out.println(item.getMetadata().getNamespace() + ":" + item.getMetadata().getName());
        }
    }

    private static void createServiceTest(){
        if (!new File(kubeConfigPath).exists()) {
            System.out.println("kubeConfig不存在，跳过");
            return;
        }
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        String namespace = "default";
        String serviceName = "my-nginx-service";
        Integer port = 80;
        Map<String, String> selector = new HashMap<>();
        selector.put("run", "my-nginx");
        V1Service v1Service = k8sClient.createService(namespace, serviceName, port, selector);
        System.out.println(v1Service != null ? v1Service.getMetadata() : null);
    }

    private static void createV1IngressTest(){
        if (!new File(kubeConfigPath).exists()) {
            System.out.println("kubeConfig不存在，跳过");
            return;
        }
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        String namespace = "default";
        String ingressName = "my-nginx-ingress";
        Map<String, String> annotations = new HashMap<>();
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/");
        String path = "/my-nginx";
        String serviceName = "my-nginx-service";
        Integer servicePort = 80;
        V1Ingress v1Ingress = k8sClient.createV1Ingress(namespace, ingressName, annotations, path, serviceName, servicePort);
        System.out.println(v1Ingress != null ? v1Ingress.getMetadata() : null);
    }

    private static void createExtensionIngressTest(){
        if (!new File(kubeConfigPath).exists()) {
            System.out.println("kubeConfig不存在，跳过");
            return;
        }
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        String namespace = "default";
        String ingressName = "my-nginx-ingress";
        Map<String, String> annotations = new HashMap<>();
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/");
        String path = "/my-nginx";
        String serviceName = "my-nginx-service";
        Integer servicePort = 80;
        ExtensionsV1beta1Ingress extensionsV1beta1Ingress = k8sClient.createExtensionIngress(namespace, ingressName, annotations, path, serviceName, servicePort);
        System.out.println(extensionsV1beta1Ingress != null ? extensionsV1beta1Ingress.getMetadata() : null);
    }

}
