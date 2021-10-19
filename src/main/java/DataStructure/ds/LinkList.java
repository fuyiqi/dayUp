package DataStructure.ds;

import org.testng.annotations.Test;

/**
 * @Description: 单链表的节点
 */
class LinkNode {

    public int elem;
    public LinkNode next;

    public LinkNode(int elem) {
        this.elem = elem;
        this.next = null;
    }

    public LinkNode() {
        this.elem = Integer.MIN_VALUE;
        this.next = null;
    }

    public String toString(){
        return "elem="+ this.elem +",next="+ this.next.elem;
    }
}



/**
 * @Description: 带头节点的单链表（头节点不计入）
 */
public class LinkList {

/********************常规操作【start】****************************/

    /**
     * @Description: 带头节点的单链表长度
     * **/
    public int getLength(LinkNode node){
        int cnt = 0;
        LinkNode curNode = node;
        while (curNode!=null){
            cnt++;
            curNode=curNode.next;
        }
        return cnt;
    }


    /**
     * @Description:打印链表
     * **/
    public void show(LinkNode node){
        LinkNode cur = node;
        while (cur!=null){
            System.out.print(cur.elem+"\t");
            cur = cur.next;
        }

    }

    /**
     * @Description: 先进先出的创建链表<尾插法>
     * **/
    public LinkNode initByTail(int[] nums){
        LinkNode headNode = new LinkNode();
        LinkNode curNode = headNode;
        for(int e:nums){
            LinkNode tmpNode = new LinkNode(e);
            curNode.next=tmpNode;
            curNode=tmpNode;
        }
        return headNode;
    }

    /**
     * @Description: 先进后出的创建链表<头插法>
     * **/
    public LinkNode initByHead(int[] nums){
        LinkNode headNode = new LinkNode();
        for(int e: nums){
            LinkNode tmpNode = new LinkNode(e);
            LinkNode oldFirstNode = headNode.next;
            headNode.next = tmpNode;
            tmpNode.next = oldFirstNode;
        }

        return headNode;
    }

    /**
     * @Description: 删除索引位置的元素<头节点的位置是0>
     * **/
    public void delByPos(LinkNode node,int pos){
        if(pos<0||pos>getLength(node)){
            System.out.println("索引不合法");
            return;
        }
        LinkNode curNode = node;int cnt=0;
        while (curNode.next!=null){
            if(pos==cnt+1){//命中要删除的元素位置
                LinkNode delTarget = curNode.next;
                LinkNode delNext = delTarget.next;
                curNode.next=delNext;
                return;
            }else {
                cnt++;
                curNode=curNode.next;
            }
        }
    }


/********************常规操作【end】****************************/


    /**
     * @Description: 原地逆置带头节点的单链表
     * **/
    public void reverseLocal(LinkNode node){
        if(getLength(node)<=2){
            System.out.println("原地逆置链表长度小于3");
            return;
        }
        LinkNode firstNode = node.next;
        LinkNode curNode = firstNode.next;

        while (curNode!=null){
            LinkNode tmpNode = curNode;
            curNode=curNode.next;
            tmpNode.next=node.next;
            node.next=tmpNode;
        }
        firstNode.next=null;
    }

    public void reverseLocal2(LinkNode node){
        int linkListLength = getLength(node);
        if(linkListLength<=2){
            System.out.println("原地逆置链表长度小于3");
            return;
        }
        LinkNode firstNode = node.next;
        LinkNode curNode = firstNode.next;
        for(int i =2;i<linkListLength;i++){
            LinkNode tmpNode = curNode;
            curNode=curNode.next;
            tmpNode.next=node.next;
            node.next=tmpNode;
        }
        firstNode.next=null;
    }



    /**
     * @Description: 原地逆置带头节点的单链表的部分元素
     * **/
    public void partialReverseLocal(LinkNode node, int start,int end){
        int linkListLength = getLength(node);
        if(linkListLength<=2){
            System.out.println("原地逆置链表长度小于3");
            return;
        }
        if(!(start<=end && end<=linkListLength)){
            System.out.println("原地逆置链表的起始或终止位置非法");
            return;
        }

        LinkNode curNode = node;
        //找到start的前驱
        for(int i=0;i<start-1;i++){
            curNode=curNode.next;
        }
        //构建带头节点的单链表
        LinkNode headNode = new LinkNode();
        headNode.next=curNode.next;
        //开始原地逆置
        LinkNode firstNode = headNode.next;
        curNode = firstNode.next;
        for(int i =0;i<end-start;i++){
            LinkNode tmpNode = curNode;
            curNode=curNode.next;
            tmpNode.next=headNode.next;
            headNode.next=tmpNode;
        }
        firstNode.next=curNode;

        //去掉增加的头节点
        curNode = node;
        for(int i=0;i<start-1;i++){
            curNode=curNode.next;
        }
        curNode.next = headNode.next;
    }


    /**
     * @Description: 合并两个排序的链表
     */
    public LinkNode mergeTwoLists(LinkNode l1,LinkNode l2){
        LinkNode cur1=l1.next;
        LinkNode cur2=l2.next;
        LinkNode res = new LinkNode();
        LinkNode cur=res;
        while (cur1.next!=null&&cur2.next!=null){
            while(cur1.elem<=cur2.elem&&cur1.next!=null&&cur2.next!=null){
                LinkNode tmp = new LinkNode(cur1.elem);
                cur.next=tmp;cur=tmp;
                cur1=cur1.next;
            }
            while(cur2.elem<cur1.elem&&cur1.next!=null&&cur2.next!=null){
                LinkNode tmp = new LinkNode(cur2.elem);
                cur.next=tmp;cur=tmp;
                cur2=cur2.next;
            }
        }


        if(cur1.next==null){
            System.out.print(cur1.elem+"\t");
            while (cur2!=null){
                LinkNode tmp = new LinkNode(cur2.elem);
                cur.next=tmp;cur=tmp;
                cur2=cur2.next;
            }

        }else {
            if(cur2.next==null){
                System.out.print(cur2.elem+"\t");
                while (cur1!=null){
                    LinkNode tmp = new LinkNode(cur1.elem);
                    cur.next=tmp;cur=tmp;
                    cur1=cur1.next;
                }
            }

        }

        return res;
    }




    private LinkList getIntersectionNode(){


        return null;
    }





    @Test
    public void test(){
        int[] nums = {1,2,3,4,5,6,7};
        LinkNode node = initByTail(nums);

        //System.out.println(getLength(node));
        partialReverseLocal(node,3,6);
        show(node);
    }


    @Test
    public void t2(){
        int[] a1={1,5,9,290,301,489,560};
        int[] a2={2,99,100,101};

        LinkNode l1 = initByTail(a1);
        LinkNode l2 = initByTail(a2);
        LinkNode res = mergeTwoLists(l1,l2);
        show(res);

    }



}
