package com.lagou.demo;

import com.lagou.demo.classloader.MyClassloader;
import com.lagou.demo.connector.Context;
import com.lagou.demo.connector.Host;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName Bootstrap
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/27 9:51
 **/
public class Bootstrap {
    private int port = 8080;

    private Map<String, HttpServlet> servletMap = new HashMap<>();
    private Host host;


    public void start() throws Exception {
        //解析server的xml文件
        loadServerXml();
        //解析servlet,加载配置信息
        loadServlet();
        ThreadPoolExecutor threadPoolExecutor = createThreadPool();

        ServerSocket serverSocket = new ServerSocket(port);
        //MinicatV3.0版本--返回静态资源加动态资源servlet(直接用线程开销很大,我们用线程池来解决一直创建新线程的开销问题)
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            threadPoolExecutor.execute(requestProcessor);
        }
    }


    /**
     * 创建线程池,解决一直创建线程开销很大的问题
     */
    private ThreadPoolExecutor createThreadPool() {
        //核心线程数
        int corePoolSize = 10;
        //最大线程数
        int maximumPoolSize = 50;
        //线程超时时间
        long keepAliveTime = 100;
        //线程超时的单位
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(50);
        //线程工厂,用来创建线程
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //线程的拒绝策略
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);
        return threadPoolExecutor;
    }

    /**
     * 解析server的xml文件
     */
    private void loadServerXml() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(inputStream);
            Element rootElement = document.getRootElement();
            //从根节点的儿子节点中获取名称为servlet的节点
            Element serviceElement = (Element) rootElement.selectNodes("//Service").get(0);
            Element hostElement = (Element) serviceElement.selectSingleNode("//Host");
            String hostName = hostElement.attributeValue("name");
            String appBase = hostElement.attributeValue("appBase");
            //设置host属性值
            Host host = new Host();
            host.setName(hostName);
            host.setAppBase(appBase);
            List<Context> contextList = new ArrayList<>();
            Context context = null;
            List<Element> contextElementlist = hostElement.selectNodes("//Context");
            for (Element element : contextElementlist) {
                context = new Context();
                String docBase = element.attributeValue("docBase");
                String path = element.attributeValue("path");
                context.setDocBase(docBase);
                context.setPath(path);
                contextList.add(context);
            }
            host.setContextList(contextList);
            this.host = host;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析servlet,加载配置信息
     */
    private void loadServlet() {
        for (int i = 0; i < host.getContextList().size(); i++) {
            String url = host.getAppBase() + "/" + host.getContextList().get(i).getDocBase() + "/web.xml";
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(url);
            SAXReader reader = new SAXReader();
            Document document = null;
            try {
                document = reader.read(inputStream);
                Element rootElement = document.getRootElement();
                //从根节点的儿子节点中获取名称为servlet的节点
                List<Element> list = rootElement.selectNodes("//servlet");
                for (Element element : list) {
                    Element servletNameElement = (Element) element.selectSingleNode("//servlet-name");
                    String servletNameValue = servletNameElement.getStringValue();
                    Element servletClassElement = (Element) element.selectSingleNode("//servlet-class");
                    String servletClassValue = servletClassElement.getStringValue();
                    // 根据servlet-name的值找到url-pattern
                    Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletNameValue + "']");
                    String urlPatternValue = servletMapping.selectSingleNode("//url-pattern").getStringValue();
                    urlPatternValue = host.getContextList().get(i).getPath() + urlPatternValue;
                    String path = Class.class.getClass().getResource("/").getPath();
                    path = path.replaceAll("\\\\", "/") + host.getAppBase() + "/" + host.getContextList().get(i).getDocBase() + "/";
                    //自定义类加载器加载对应资源的类
                    MyClassloader myClassloader = new MyClassloader(path);
                    Class aClass = myClassloader.findClass(servletClassValue);
                    //加载demo1和demo2项目的servlet类
                    servletMap.put(urlPatternValue, (HttpServlet)aClass.newInstance());
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        System.out.println("Minicat----------开始启动");
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
            System.out.println("Minicat----------启动结束");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
