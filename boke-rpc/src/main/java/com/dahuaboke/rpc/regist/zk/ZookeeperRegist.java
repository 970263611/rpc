package com.dahuaboke.rpc.regist.zk;

import com.dahuaboke.rpc.regist.RegistCenter;
import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperRegist implements RegistCenter {

    private CountDownLatch latch = new CountDownLatch(1);
    private String rpc_regist_address;

    public ZookeeperRegist() {
    }

    public ZookeeperRegist(String address){
        this.rpc_regist_address = address;
    }

    /**
     * 创建zookeeper连接
     * @param data
     */
    public void register(String data, String servicePath){
        if (data != null){
            ZooKeeper zk = connectServer();
            if (zk != null){
                createNode(zk, data, servicePath);
            }
        }
    }

    /**
     * 连接zk
     * @return
     */
    private ZooKeeper connectServer(){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(rpc_regist_address, 360000, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            System.out.println(e);
        }
        return zk;
    }

    /**
     * 创建节点
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data, String servicePath){
        try {
            //创建父节点
            if (zk.exists("/boke-registry", null) == null){
                zk.create("/boke-registry", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                zk.create("/boke-registry/service/", "".getBytes(),  ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }

            //创建子节点
            //String path = zk.create("/boke-registry/service/" + data, servicePath.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            String path = zk.create("/boke-registry/service/" + data + "#" + servicePath, "".getBytes(),  ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("create zookeeper node " + path + data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<String> getChildren(){
        ZooKeeper zk = connectServer();
        List<String> childrens = null;
        try{
            childrens = zk.getChildren("/boke-registry/service", false);
        }catch(Exception e){
            System.out.println("zookeeper寻找节点失败");
        }
        if(null==childrens ||childrens.size()==0){
            return null;
        }
        return childrens;
    }
}
