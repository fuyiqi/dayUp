package tricks;

import org.testng.annotations.Test;

/**
 * @Description: 单链表的节点
 */
class LinkNode {

    public int elem;
    public LinkNode next;

    public LinkNode(int elem) {
        this.elem = elem;
    }

    public LinkNode() {
        this.elem = Integer.MIN_VALUE;
        this.next = null;
    }

    public String toString(){
        return "elem="+this.elem;
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






    }







    @Test
    public void test(){
        int[] nums = {1,2,3,4,5,6,7};
        LinkNode node = initByTail(nums);

        //System.out.println(getLength(node));
        delByPos(node,3);
        show(node);
    }






}
