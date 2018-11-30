package com.hai;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hai.enums.ZKOperartionType;
import com.hai.pojo.Bgm;
import com.hai.service.BgmService;
import com.hai.util.JsonUtils;

@Component
public class ZKCuratorClient {
	
//	public static final String CONNECT_SERVER = "192.168.139.128:2181";
	
	@Autowired
	private Resource resource;
	
	// zk客户端
	private CuratorFramework client = null;
	public static final Logger log =LoggerFactory.getLogger(ZKCuratorClient.class);
	
	@Autowired
	private BgmService bgmService;
	
	//初始化客户端
	public void init() {
		
		System.out.println(resource.getConnectserver());
		
		if(client != null) {
			return;
		}
		
		//重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
		//创建zk客户端
		client =  CuratorFrameworkFactory.builder().connectString(resource.getConnectserver())
				.sessionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("admin").build();
		client.start();
		try {
//			String data=new String(client.getData().forPath("/bgm/181118HRK0H0SAY8"));
//			log.info("测试节点的数据:{}",data);
				
			addChildWatch("/bgm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void addChildWatch(String nodePath) {
		
		//拿到包含监听器的类
		final PathChildrenCache cache = new PathChildrenCache(client, nodePath, true);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
				
				if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
					log.info("监听到事件类型{}",PathChildrenCacheEvent.Type.CHILD_ADDED);
					
					//1.从节点上读取bgmId
					String path = event.getData().getPath();
					String[] strings = path.split("/");
					String bmgId = strings[strings.length-1];
					//如果是操作类型为1则添加BGM,为2则删除bgm,这里已经改造为map
					String mapper = null;
					try {
						mapper = new String(event.getData().getData(),"utf-8");
						log.info("请求得到的mapper: "+ mapper);
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					
					Map<String,String> map=JsonUtils.jsonToPojo(mapper, Map.class);
					String type = map.get("type");
					String relativeBgmPath = map.get("bgmPath");
					log.info("bgmId{}",bmgId);
					
					
					String localbgmPath= resource.getLocalbgmPath();
					
					//2.根据bgmId从数据库中查找bgm,获取相对路径
					try {
						if(ZKOperartionType.ADD.type.equals(type)) {
							Bgm bgm = bgmService.getBgm(bmgId);
							log.info("bgm对象：{}",bgm);
							//3.定义URL
							//这里应该处理url，因为本地的bgm路径可能带有中文
							String bgmPath=bgm.getPath();
							String url = resource.getDownLoadBgmPath()+processCode(bgmPath);
							//4.定义bgm保存在本地路径
							String savePath = localbgmPath+bgmPath;
							log.info("url：{}",url);
							URL bgmURL = null;
							bgmURL = new URL(url);
							FileUtils.copyURLToFile(bgmURL, new File(savePath));
							client.delete().forPath(path);
						
						}else if(ZKOperartionType.DEL.type.equals(type)){
							//不删除本地的bgm和删除节点
							String localabsolutePath =localbgmPath+relativeBgmPath;
							FileUtils.forceDelete(new File(localabsolutePath));
							client.delete().forPath(path);
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		try {
			cache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 处理请求路径带有中文的问题
	 * @param code
	 * @return
	 */
	private String processCode(String code) {
		String[] split = code.split("/");
		String encode=null;
		for (String s : split) {
			try {
				encode = URLEncoder.encode(s, "utf-8");
				encode = "/"+encode;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return encode;
	}
}


